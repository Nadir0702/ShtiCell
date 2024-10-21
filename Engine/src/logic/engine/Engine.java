package logic.engine;

import dto.cell.CellDTO;
import dto.cell.CellStyleDTO;
import dto.filter.FilterParametersDTO;
import dto.permission.PermissionDTO;
import dto.permission.SentPermissionRequestDTO;
import dto.range.RangeDTO;
import dto.range.RangesDTO;
import dto.returnable.EffectiveValueDTO;
import dto.sheet.ColoredSheetDTO;
import dto.sheet.SheetAndRangesDTO;
import dto.sheet.SheetDTO;
import dto.sheet.SheetMetaDataDTO;
import dto.version.VersionChangesDTO;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

public interface Engine {
    void loadData(String path);
    void loadDataFromStream(InputStream stream);
    String getName();
    ColoredSheetDTO getColoredSheetAsDTO(String username);
    SheetDTO getSheetAsDTO(String username);
    CellDTO getSingleCellData(String cellID, String username);
    void updateSingleCellData(String cellID, String value, String username);
    VersionChangesDTO showVersions();
    SheetAndRangesDTO getSheetVersionAsDTO(int version, String username);
    boolean isSheetLoaded();
    void loadFromFile(String path);
    void saveToFile(String path);
    RangeDTO addRange(String rangeName, String range);
    void removeRange(String rangeName);
    RangesDTO getAllRanges(String username);
    void updateCellStyle(CellStyleDTO cellStyle);
    ColoredSheetDTO sortRangeOfCells(String range, List<String> columnsToSortBy);
    ColoredSheetDTO filterRangeOfCells(FilterParametersDTO filterParameters);
    List<String> getColumnsListOfRange(String rangeToFilter);
    List<EffectiveValueDTO> getUniqueItemsToFilterBy(String columnToFilterBy, String rangeToFilter);
    SheetMetaDataDTO getSheetMetaData(String currentUserName);
    LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> getGraphFromRange(String rangeToBuildGraphFrom);
    void createNewPermissionRequest(SentPermissionRequestDTO requestToSend, String sender);
    List<PermissionDTO> getAllPermissions();
    void updatePermissionForUser(String sender, boolean answer, int requestID);
    void updateActiveUserVersion(String username);
    boolean isPermitted(String username);
    boolean isInLatestVersion(String username);
    Object getSheetEditLock();
}
