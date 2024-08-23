package component.sheet.impl;

import component.cell.api.Cell;
import component.cell.impl.CellImpl;
import component.sheet.api.Sheet;
import component.sheet.topological.order.TopologicalOrder;
import dto.SheetDTO;

import java.io.*;
import java.util.*;


public class SheetImpl implements Sheet {
    private final String sheetName;
    private Layout layout;
    private Map<String, Cell> cells;
    private int version;
    private int numOfCellsUpdated;

    public class Layout implements Serializable {
        private final int row;
        private final int column;
        private final int rowHeight;
        private final int columnWidth;

        public Layout(int row, int column, int rowHeight, int columnWidth) {
            this.row = row;
            this.column = column;
            this.rowHeight = rowHeight;
            this.columnWidth = columnWidth;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public int getRowHeight() {
            return rowHeight;
        }

        public int getColumnWidth() {
            return columnWidth;
        }
    }

    public SheetImpl(String name) {
        this.cells = new HashMap<String, Cell>();
        this.version = 1;
        this.numOfCellsUpdated = 0;
        this.layout = new Layout(6, 5, 2, 5);
        this.sheetName = name;
    }

    @Override
    public Cell getCell(String cellId) {
        if (cellInLayout(cellId)){
            return this.cells.get(cellId);
        }
        throw new IllegalArgumentException("The sheet size is " + this.layout.getRow() + " rows and " +
                this.layout.getColumn() + " columns, The entered cell ID (" + cellId + ") is out of bounds.");
    }

    private boolean cellInLayout(String cellId) {
        int row = this.parseCellIdRow(cellId);
        int column = this.parseCellIdColumn(cellId);

        return row <= this.layout.getRow()
                && row >= 0
                && column <= this.layout.getColumn()
                && column >= 0;

    }

    private int parseCellIdRow(String cellId) {
        return Integer.parseInt(cellId.substring(1)) - 1;
    }

    private int parseCellIdColumn(String cellId) {
        return cellId.charAt(0) - 'A';
    }

    @Override
    public Sheet updateCellValueAndCalculate(String cellId, String value) {
        SheetImpl newSheetVersion = copySheet();
        Cell newCell = new CellImpl(cellId, value, newSheetVersion.getVersion() + 1, newSheetVersion);
        newSheetVersion.cells.put(cellId, newCell);

        try {
            List<Cell> cellsThatHaveChanged =
                    newSheetVersion
                            .orderCellsForCalculation()
                            .stream()
                            .filter(Cell::calculateEffectiveValue)
                            .toList();

            // successful calculation. update sheet and relevant cells version
            int newVersion = newSheetVersion.increaseVersion();
            cellsThatHaveChanged.forEach(cell -> cell.updateVersion(newVersion));
            this.numOfCellsUpdated = cellsThatHaveChanged.size();
            SheetDTO newSheetVersionDTO = new SheetDTO(newSheetVersion);


            return newSheetVersion;

        } catch (Exception e) {
            // deal with the runtime error that was discovered as part of invocation
            return this;
        }
    }

    private int increaseVersion() {
         return ++this.version;
    }

    private List<Cell> orderCellsForCalculation() {
        return TopologicalOrder.SORT.topologicalSort(this.getCells());
    }

    private SheetImpl copySheet() {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            return (SheetImpl) ois.readObject();
        } catch (Exception e) {
            // deal with the runtime error that was discovered as part of invocation
            // CATCH IN THE UI
            throw new RuntimeException(e);
        }
    }

    @Override
    public Layout getLayout() {
        return layout;
    }

    @Override
    public int getVersion() {
        return this.version;
    }

    @Override
    public String getSheetName(){
        return this.sheetName;
    }

    @Override
    public Map<String, Cell> getCells(){
        return this.cells;
    }

    @Override
    public int getNumOfCellsUpdated(){
        return this.numOfCellsUpdated;
    }
}
