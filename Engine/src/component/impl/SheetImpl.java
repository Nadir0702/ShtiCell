package component.impl;

import component.api.Cell;
import component.api.Sheet;
import java.util.HashMap;
import java.util.Map;

public class SheetImpl implements Sheet {
    private String sheetName;
    private String xmlPath;
    private Layout layout;
    private Map<String, Cell> cells;
    private int version;

    private class Layout {
        private int row;
        private int column;
        private int rowHeight;
        private int columnWidth;

    }

    public SheetImpl() {
        this.cells = new HashMap<String, Cell>();
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public Cell getCell(int row, int col) {
        return this.cells.get(Cell.createCellId(row, col));
    }

    @Override
    public void setCell(int row, int col, String value) {
        Cell cell = this.cells.get(Cell.createCellId(row, col));
        cell.setOriginalValue(value);
    }
}
