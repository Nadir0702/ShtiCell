package component.sheet.api;

import component.cell.api.Cell;
import component.sheet.impl.SheetImpl;

import java.util.Map;

public interface ReadonlySheet {
    int getVersion();
    Cell getCell(String cellId);
    SheetImpl.Layout getLayout();
    String getSheetName();
    Map<String, Cell> getCells();
    int getNumOfCellsUpdated();


}
