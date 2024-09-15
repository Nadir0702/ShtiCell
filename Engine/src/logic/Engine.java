package logic;

import dto.CellDTO;
import dto.SheetDTO;
import dto.VersionChangesDTO;

public interface Engine {
    void loadData(String path);
    SheetDTO getSheetAsDTO();
    CellDTO getSingleCellData(String cellID);
    void updateSingleCellData(String cellID, String value);
    VersionChangesDTO showVersions();
    SheetDTO getSheetVersionAsDTO(int version);
    boolean isSheetLoaded();
    void loadFromFile(String path);
    void saveToFile(String path);
    void addRange(String rangeName, String range);
}
