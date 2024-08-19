package dto;

import component.api.Cell;
import component.api.CellType;
import component.impl.SheetImpl;
import logic.function.returnable.MyBoolean;
import logic.function.returnable.MyNumber;
import logic.function.returnable.MyString;
import logic.function.returnable.Returnable;

import java.util.HashMap;
import java.util.Map;

public class SheetDTO {
    private final String sheetName;
    private final SheetImpl.Layout layout;
    private final Map<String, Returnable> cells;
    private final int version;
    private final int numOfCellsUpdated;

    public SheetDTO(String sheetName, SheetImpl.Layout layout, Map<String, Cell> cells, int version, int numOfCellsUpdated) {
        this.sheetName = sheetName;
        this.layout = layout;
        this.version = version;
        this.numOfCellsUpdated = numOfCellsUpdated;
        this.cells = new HashMap<>();

        for (Cell cell : cells.values()) {
//            if(cell.getEffectiveValue().getReturnType().equals(CellType.NUMERIC)) {
//                this.cells.put(cell.getCellId(), new MyNumber(cell.getEffectiveValue().tryConvertTo(Double.class)));
//            } else if(cell.getEffectiveValue().getReturnType().equals(CellType.BOOLEAN)) {
//                this.cells.put(cell.getCellId(), new MyBoolean(cell.getEffectiveValue().tryConvertTo(Boolean.class)));
//            } else {
//                this.cells.put(cell.getCellId(), new MyString(cell.getEffectiveValue().tryConvertTo(String.class)));
//            }
            this.cells.put(cell.getCellId(), cell.getEffectiveValue());
        }
    }

    public String getSheetName() {
        return sheetName;
    }

    public SheetImpl.Layout getLayout() {
        return layout;
    }

    public Map<String, Returnable> getCells() {
        return cells;
    }

    public int getVersion() {
        return version;
    }

    public int getNumOfCellsUpdated() {
        return numOfCellsUpdated;
    }
}
