package logic;

import dto.*;
import javafx.scene.paint.Color;

import java.util.List;

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
    RangeDTO addRange(String rangeName, String range);
    void removeRange(String rangeName);
    RangesDTO getAllRanges();
    void updateCellStyle(String cellID, Color backgroundColor, Color textColor);
    SheetDTO sortRangeOfCells(String range, List<String> columnsToSortBy);
}
