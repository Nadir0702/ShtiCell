package logic;

import dto.SheetDTO;

public interface Engine {
    boolean LoadData(String path);
    SheetDTO getSheetAsDTO();
    void getSingleCellData(String cellID);
    void updateSingleCellData(String cellID);
    void showVersions();
}
