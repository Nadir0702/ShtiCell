package logic.engine;

import component.archive.api.Archive;
import component.archive.impl.ArchiveImpl;
import component.cell.api.Cell;
import component.cell.impl.CellImpl;
import component.range.api.Range;
import component.range.impl.RangeImpl;
import component.sheet.api.Sheet;
import component.sheet.impl.SheetImpl;
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
import jakarta.xml.bind.JAXBException;
import javafx.scene.paint.Color;
import jaxb.converter.api.XMLToSheetConverter;
import jaxb.converter.impl.XMLToSheetConverterImpl;
import logic.filter.Filter;
import logic.function.returnable.api.Returnable;
import logic.graph.GraphSeriesBuilder;
import user.User;
import user.permission.PermissionStatus;
import user.permission.PermissionType;
import logic.sort.Sorter;
import user.request.PermissionRequest;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EngineImpl implements Engine{
    private final User owner;
    private String name;
    private Sheet sheet;
    private Archive archive;
    private final Map<String, PermissionType> usersPermissions;
    private final List<PermissionRequest> allPermissionRequests;
    private final ReadWriteLock usersPermissionLock;
    private final ReadWriteLock allPermissionLock;
    
    public EngineImpl(User owner) {
        this.owner = owner;
        this.name = null;
        this.sheet = null;
        this.archive = null;
        
        this.usersPermissions = new HashMap<>();
        this.allPermissionRequests = new LinkedList<>();
        
        this.usersPermissions.put(this.owner.getUserName(),PermissionType.OWNER);
        this.allPermissionRequests.add(new PermissionRequest(
                this.owner.getUserName(),
                PermissionType.OWNER,
                PermissionStatus.APPROVED,
                this.allPermissionRequests.size())
        );
        
        this.usersPermissionLock = new ReentrantReadWriteLock();
        this.allPermissionLock = new ReentrantReadWriteLock();
    }
    
    @Override
    public void loadData(String path) {
        try {
            XMLToSheetConverter converter = new XMLToSheetConverterImpl();
            this.sheet = converter.convert(path);
            this.archive = new ArchiveImpl();
            this.archive.storeInArchive(this.sheet.copySheet());
        } catch (JAXBException | FileNotFoundException e ) {
            throw new RuntimeException("Error loading data from file", e);
        }
    }
    
    @Override
    public void loadDataFromStream(InputStream stream) {
        try {
            XMLToSheetConverter converter = new XMLToSheetConverterImpl();
            this.sheet = converter.convertFromStream(stream);
            this.archive = new ArchiveImpl();
            this.archive.storeInArchive(this.sheet.copySheet());
            this.name = this.sheet.getSheetName();
        } catch (JAXBException | FileNotFoundException e ) {
            throw new RuntimeException("Error loading data from file", e);
        }
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public SheetDTO getSheetAsDTO() {
        return new SheetDTO(this.sheet);
    }

    @Override
    public CellDTO getSingleCellData(String cellID) {
        return new CellDTO(this.sheet.getCell(cellID), cellID);
    }

    @Override
    public void updateSingleCellData(String cellID, String value) {
        Cell cellToUpdate = this.sheet.getCell(cellID);
        boolean isUpdatingEmptyToEmpty = cellToUpdate == null && value.isEmpty();
        boolean isOriginalValueChanged = cellToUpdate != null && !cellToUpdate.getOriginalValue().equals(value);
        
        if (isUpdatingEmptyToEmpty) {
            return;
        }
        
        SheetImpl newSheetVersion = this.sheet.copySheet();
        updateCell(cellID, value, newSheetVersion);
        Sheet tempSheet = this.sheet.updateSheet(newSheetVersion, isOriginalValueChanged);
        if (!tempSheet.equals(this.sheet)) {
            this.sheet = tempSheet;
            this.archive.storeInArchive(this.sheet.copySheet());
        }
    }

    private void updateCell(String cellID, String value, Sheet newSheetVersion) {
        Cell cellToUpdate = newSheetVersion.getCell(cellID);
        if (cellToUpdate != null) {
            cellToUpdate.getUsedRanges().forEach(range -> {
                Range currentRange = newSheetVersion.getRanges().get(range);
                if (currentRange != null) {
                    currentRange.reduceUsage();
                }
            });
            cellToUpdate.setOriginalValue(value, newSheetVersion.getVersion() + 1);
        } else {
            cellToUpdate = new CellImpl(cellID, value, newSheetVersion.getVersion(), newSheetVersion);
            newSheetVersion.getCells().put(cellToUpdate.getCellId(), cellToUpdate);
        }
    }

    @Override
    public VersionChangesDTO showVersions() {
        return new VersionChangesDTO(this.archive.getAllVersionsChangesList());
    }

    @Override
    public ColoredSheetDTO getSheetVersionAsDTO(int version) {
        return new ColoredSheetDTO(this.archive.retrieveVersion(version));
    }

    @Override
    public boolean isSheetLoaded() {
        return this.sheet != null;
    }

    @Override
    public void loadFromFile(String path) {
        this.archive = Archive.loadFromFile(path);
        this.sheet = this.archive.retrieveLatestVersion();
    }

    @Override
    public void saveToFile(String path) {
        this.archive.saveToFile(path);
    }
    
    @Override
    public RangeDTO addRange(String rangeName, String range) {
        this.sheet.createRange(rangeName, range);
        return new RangeDTO(this.sheet.getRanges().get(rangeName));
    }
    
    @Override
    public void removeRange(String rangeName) {
        this.sheet.deleteRange(rangeName);
    }
    
    @Override
    public RangesDTO getAllRanges() {
        return new RangesDTO(this.sheet.getRanges());
    }
    
    @Override
    public void updateCellStyle(String cellID, Color backgroundColor, Color textColor) {
        if(this.sheet.getCell(cellID) == null) {
            Cell cell = new CellImpl(cellID, "", this.sheet.getVersion(), this.sheet);
            this.sheet.getCells().put(cell.getCellId(), cell);
        }
        
        this.sheet.getCell(cellID).setBackgroundColor(backgroundColor);
        this.sheet.getCell(cellID).setTextColor(textColor);
    }
    
    @Override
    public ColoredSheetDTO sortRangeOfCells(String range, List<String> columnsToSortBy) {
        Sheet sortedSheet = this.sheet.copySheet();
        Sorter sorter = new Sorter(new RangeImpl("sort", range, sortedSheet), columnsToSortBy);
        
        sorter.sort().getRangeCells().forEach(cell -> sortedSheet.getCells().put(cell.getCellId(), cell));
        
        return new ColoredSheetDTO(sortedSheet);
    }
    
    @Override
    public List<String> getColumnsListOfRange(String range) {
        Range rangeToFilter = new RangeImpl("range of columns", range, this.sheet.copySheet());
        
        return rangeToFilter.getColumnsListOfRange();
    }
    
    @Override
    public List<EffectiveValueDTO> getUniqueItemsToFilterBy(String column, String rangeName) {
        Range range = new RangeImpl("range of unique items", rangeName, this.sheet.copySheet());
        
        return this.getUniqueItemsInColumn(column, range);
    }
    
    @Override
    public SheetMetaDataDTO getSheetMetaData(String currentUserName) {
        PermissionType permission;
        
        this.usersPermissionLock.readLock().lock();
        try {
            permission = this.usersPermissions.get(currentUserName);
        } finally {
            usersPermissionLock.readLock().unlock();
        }
        
        if (permission == null) {
            permission = PermissionType.NONE;
        }
        
        return new SheetMetaDataDTO(
                this.owner.getUserName(),
                this.sheet.getSheetName(),
                this.sheet.getLayout().getRow(),
                this.sheet.getLayout().getColumn(),
                permission.getPermission()
        );
    }
    
    @Override
    public LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> getGraphFromRange(String rangeToBuildGraphFrom) {
        GraphSeriesBuilder graphSeries = new GraphSeriesBuilder(new RangeImpl("range of graph", rangeToBuildGraphFrom, this.sheet.copySheet()));

        return graphSeries.build();
    }
    
    @Override
    public void updatePermissionForUser(String sender, boolean answer, int requestID) {
        this.allPermissionLock.writeLock().lock();
        try {
            PermissionRequest requestToAnswerTo = this.allPermissionRequests.get(requestID);
            
            if (requestToAnswerTo == null) {
                throw new IllegalArgumentException("No Permission Request found with ID " + requestID);
            }
            
            if (answer) {
                this.usersPermissionLock.writeLock().lock();
                try {
                    this.usersPermissions.put(sender, requestToAnswerTo.getRequestedPermission());
                } finally {
                    this.usersPermissionLock.writeLock().unlock();
                }
            }
            
            requestToAnswerTo.updateRequestStatus(answer);
        } finally {
            this.allPermissionLock.writeLock().unlock();
        }
    }
    
    @Override
    public List<PermissionDTO> getAllPermissions() {
        List<PermissionDTO> permissions;
        
        this.allPermissionLock.readLock().lock();
        try {
            permissions = new ArrayList<>(this.allPermissionRequests.size());
            
            this.allPermissionRequests.forEach((permission) ->
                    permissions.add(new PermissionDTO(
                            permission.getSenderName(),
                            permission.getRequestedPermission().getPermission(),
                            permission.getRequestStatus().getPermissionStatus()))
            );
        } finally {
            this.allPermissionLock.readLock().unlock();
        }
        
        return permissions;
    }
    
    @Override
    public void createNewPermissionRequest(SentPermissionRequestDTO requestToSend, String sender)  throws IllegalArgumentException {
        if (this.owner.getUserName().equals(sender)) {
            throw new IllegalArgumentException("Cannot create a new permission request for your own Sheets");
        }
        
        PermissionRequest permissionRequest, copyRequest;
        
        this.allPermissionLock.writeLock().lock();
        try {
            permissionRequest = new PermissionRequest(
                    sender,
                    PermissionType.valueOf(requestToSend.getRequestedPermission().toUpperCase()),
                    PermissionStatus.PENDING,
                    this.allPermissionRequests.size());
            
            this.allPermissionRequests.add(permissionRequest);
            copyRequest = permissionRequest.deepCopy();
        } finally {
            this.allPermissionLock.writeLock().unlock();
        }
        
        this.owner.createPermissionRequest(copyRequest, this.name, sender);
    }
    
    private List<EffectiveValueDTO> getUniqueItemsInColumn(String column, Range range) {
        List<Cell> itemsList = range.getRangeCells()
                .stream()
                .filter(cell -> cell.getCellId().contains(column))
                .toList();
        
        Set<EffectiveValueDTO> itemsSet = new LinkedHashSet<>();
        itemsList.forEach(cell -> itemsSet.add(new EffectiveValueDTO(cell.getEffectiveValue())));
        
        return new ArrayList<>(itemsSet);
    }
    
    @Override
    public ColoredSheetDTO filterRangeOfCells(String rangeToFilterBy, String columnToFilterBy, List<Integer> itemsToFilterBy) {
        Sheet filteredSheet = this.sheet.copySheet();
        Range rangeToFilter = new RangeImpl("range to filter", rangeToFilterBy, filteredSheet);
        rangeToFilter.getRangeCells().forEach(cell -> filteredSheet.getCells().remove(cell.getCellId()));
        Filter filter = new Filter(rangeToFilter);
        List<EffectiveValueDTO> uniqueItemsList = this.getUniqueItemsInColumn(columnToFilterBy, rangeToFilter);
        List<EffectiveValueDTO> filteredItemsList = new ArrayList<>();
        for (int itemToFilterIndex : itemsToFilterBy) {
            filteredItemsList.add(uniqueItemsList.get(itemToFilterIndex));
        }
        filter.filter(columnToFilterBy,filteredItemsList)
                .getRangeCells().forEach(cell -> filteredSheet.getCells().put(cell.getCellId(), cell));
        return new ColoredSheetDTO(filteredSheet);
    }
}
