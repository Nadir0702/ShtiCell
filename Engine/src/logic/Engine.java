package logic;

import dto.CellDTO;
import dto.SheetDTO;

public interface Engine {
    void LoadData(String path);
    SheetDTO getSheetAsDTO();
    CellDTO getSingleCellData(String cellID);
    void updateSingleCellData(String cellID, String value);
    void showVersions();
}
