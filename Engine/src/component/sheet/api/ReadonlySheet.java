package component.sheet.api;

import component.cell.api.Cell;
import component.sheet.impl.SheetImpl;

import java.io.Serializable;
import java.util.Map;

public interface ReadonlySheet extends Serializable {
    int getVersion();
    Cell getCell(String cellId);
    SheetImpl.Layout getLayout();
    String getSheetName();
    Map<String, Cell> getCells();
    int getNumOfCellsUpdated();
    SheetImpl copySheet();
    boolean cellInLayout(String cellId);
}
