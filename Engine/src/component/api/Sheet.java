package component.api;

import component.impl.SheetImpl;

import java.util.Map;

public interface Sheet {
    int getVersion();
    Cell getCell(String cellId);
    void setCell(String cellId, String value);
    public SheetImpl.Layout getLayout();
    public String getSheetName();
    public Map<String, Cell> getCells();
    public int getNumOfCellsUpdated();

}
