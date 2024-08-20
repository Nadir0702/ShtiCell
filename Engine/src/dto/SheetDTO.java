package dto;

import component.api.Cell;
import component.api.Sheet;
import component.impl.SheetImpl;
import logic.function.returnable.api.Returnable;

import java.util.HashMap;
import java.util.Map;

public class SheetDTO {
    private final String sheetName;
    private final SheetImpl.Layout layout;
    private final Map<String, Returnable> cells;
    private final int version;
    private final int numOfCellsUpdated;

    public SheetDTO(Sheet sheet) {
        this.sheetName = sheet.getSheetName();
        this.layout = sheet.getLayout();
        this.version = sheet.getVersion();
        this.numOfCellsUpdated = sheet.getNumOfCellsUpdated();
        this.cells = new HashMap<>();

        for (Cell cell : sheet.getCells().values()) {
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
