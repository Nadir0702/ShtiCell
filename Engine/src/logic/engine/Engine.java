package logic.engine;

import dto.cell.CellDTO;
import dto.permission.PermissionDTO;
import dto.permission.SentPermissionRequestDTO;
import dto.range.RangeDTO;
import dto.range.RangesDTO;
import dto.returnable.EffectiveValueDTO;
import dto.sheet.ColoredSheetDTO;
import dto.sheet.SheetDTO;
import dto.sheet.SheetMetaDataDTO;
import dto.version.VersionChangesDTO;
import javafx.scene.paint.Color;
import logic.function.returnable.api.Returnable;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

public interface Engine {
    void loadData(String path);
    void loadDataFromStream(InputStream stream);
    String getName();
    SheetDTO getSheetAsDTO();
    CellDTO getSingleCellData(String cellID);
    void updateSingleCellData(String cellID, String value);
    VersionChangesDTO showVersions();
    ColoredSheetDTO getSheetVersionAsDTO(int version);
    boolean isSheetLoaded();
    void loadFromFile(String path);
    void saveToFile(String path);
    RangeDTO addRange(String rangeName, String range);
    void removeRange(String rangeName);
    RangesDTO getAllRanges();
    void updateCellStyle(String cellID, Color backgroundColor, Color textColor);
    ColoredSheetDTO sortRangeOfCells(String range, List<String> columnsToSortBy);
    ColoredSheetDTO filterRangeOfCells(String rangeToFilterBy, String columnToFilterBy, List<Integer> itemsToFilterBy);
    List<String> getColumnsListOfRange(String rangeToFilter);
    List<EffectiveValueDTO> getUniqueItemsToFilterBy(String columnToFilterBy, String rangeToFilter);
    SheetMetaDataDTO getSheetMetaData(String currentUserName);
    LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> getGraphFromRange(String rangeToBuildGraphFrom);
    void createNewPermissionRequest(SentPermissionRequestDTO requestToSend, String sender);
    List<PermissionDTO> getAllPermissions();
    void updatePermissionForUser(String sender, boolean answer, int requestID);
}
