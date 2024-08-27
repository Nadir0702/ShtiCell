package logic;

import dto.CellDTO;
import dto.SheetDTO;
import dto.VersionChangesDTO;

public interface Engine {
    void LoadData(String path);
    SheetDTO getSheetAsDTO();
    CellDTO getSingleCellData(String cellID);
    void updateSingleCellData(String cellID, String value);
    VersionChangesDTO showVersions();
    SheetDTO getSheetVersionAsDTO(int version);
    boolean isSheetLoaded();
}
