package component.impl;

import component.api.Cell;
import component.api.Sheet;
import java.util.HashMap;
import java.util.Map;

public class SheetImpl implements Sheet {
    private final String sheetName;
    private Layout layout;
    private Map<String, Cell> cells;
    private int version;
    private int numOfCellsUpdated;

    public class Layout {
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

    public SheetImpl() {
        this.cells = new HashMap<String, Cell>();
        this.version = 1;
        this.numOfCellsUpdated = 0;
        this.layout = new Layout(0, 0, 0, 0);
        this.sheetName = "Sheet";
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
    public Cell getCell(String cellId) {
        return this.cells.get(cellId);
    }

    @Override
    public void setCell(String cellId, String value) {
        Cell cell = this.cells.get(cellId);
        cell.setOriginalValue(value);
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
