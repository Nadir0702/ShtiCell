package logic;

import dto.CellDTO;
import dto.SheetDTO;

public interface Engine {
    boolean LoadData(String path);
    SheetDTO getSheetAsDTO();
    CellDTO getSingleCellData(String cellID);
    void updateSingleCellData(String cellID);
    void showVersions();
}
