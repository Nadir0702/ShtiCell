package gui.main.view;

import dto.CellDTO;
import dto.RangeDTO;
import dto.RangesDTO;
import dto.SheetDTO;
import gui.Main;
import gui.action.line.ActionLineController;
import gui.cell.CellSubComponentController;
import gui.command.CommandsController;
import gui.customization.CustomizationController;
import gui.grid.GridBuilder;
import gui.grid.SheetGridController;
import gui.ranges.RangesController;
import gui.top.TopSubComponentController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.Engine;
import logic.EngineImpl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainViewController {
    
    @FXML private TopSubComponentController topSubComponentController;
    @FXML private CustomizationController customizationsController;
    @FXML private CommandsController commandsController;
    @FXML private RangesController rangesController;
    private ActionLineController actionLineController;
    private SheetGridController sheetGridController;
    private Map<String, CellSubComponentController> cellSubComponentControllerMap;
    
    private BooleanProperty fileNotLoadedProperty;
    private Engine engine;
    private Stage primaryStage;
    
    public MainViewController() {
        this.fileNotLoadedProperty = new SimpleBooleanProperty(true);
    }
    
    @FXML
    public void initialize() {
        this.engine = new EngineImpl();
        
        if (this.topSubComponentController != null) {
            this.topSubComponentController.setMainController(this);
            this.setActionLineController(this.topSubComponentController.getActionLIneController());
        }
        
        if (this.rangesController != null) {
            this.rangesController.setMainController(this);
        }
        
        if (this.customizationsController != null) {
            this.customizationsController.setMainController(this);
        }
        
        if (this.commandsController != null) {
            this.commandsController.setMainController(this);
        }
        
        this.actionLineController.bindFileNotLoaded(this.fileNotLoadedProperty);
        this.rangesController.bindFileNotLoaded(this.fileNotLoadedProperty);
        this.customizationsController.bindFileNotLoaded(this.fileNotLoadedProperty);
        this.commandsController.bindFileNotLoaded(this.fileNotLoadedProperty);
    }
    
    public void setActionLineController(ActionLineController actionLineController) {
        this.actionLineController = actionLineController;
        this.actionLineController.setMainController(this);
    }
    
    public void setSheetGridController(SheetGridController sheetGridController) {
        this.sheetGridController = sheetGridController;
        this.sheetGridController.setMainController(this);
    }
    
    public void setCellSubComponentControllerMap(
            Map<String, CellSubComponentController> cellSubComponentControllerMap) {
        
        this.cellSubComponentControllerMap = cellSubComponentControllerMap;
        this.cellSubComponentControllerMap.forEach((cellID, cellController) -> {
            cellController.setMainController(this);
        });
    }
    
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
    
    public File getFilePath(FileChooser fileChooser) {
        return fileChooser.showOpenDialog(this.primaryStage);
    }
    
    public void loadNewSheetFromXML(String absolutePath) {
        try {
            this.engine.loadData(absolutePath);
            SheetDTO sheetDTO = this.engine.getSheetAsDTO();
            RangesDTO rangesDto = this.engine.getAllRanges();
            GridBuilder gridBuilder = new GridBuilder(sheetDTO.getLayout().getRow(),
                                                      sheetDTO.getLayout().getColumn(),
                                                      sheetDTO.getLayout().getRowHeight(),
                                                      sheetDTO.getLayout().getColumnWidth());
            
            BorderPane root = (BorderPane)this.primaryStage.getScene().getRoot();
            root.setCenter(gridBuilder.build());
            this.setSheetGridController(gridBuilder.getSheetGridController());
            this.setCellSubComponentControllerMap(this.sheetGridController.getCellsControllers());
            this.sheetGridController.initializeGridModel(sheetDTO.getCells());
            
            this.rangesController.initializeRangesModel(rangesDto);
            
            this.fileNotLoadedProperty.set(false);
            this.actionLineController.resetCellModel();
            this.rangesController.resetController();
            this.customizationsController.resetController();
            this.commandsController.resetController();
            this.topSubComponentController.setSheetNameAndVersion(sheetDTO.getSheetName(), sheetDTO.getVersion());
            
        } catch (RuntimeException | IOException e) {
            // Create error popup dialog
            System.out.println("Error Loading File:\n" + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }
    
    public void showCellDetails(CellSubComponentController cellSubComponentController) {
        String selectedCellID = cellSubComponentController.cellIDProperty().get();
        if (this.sheetGridController.isAlreadySelected(selectedCellID)) {
            this.sheetGridController.resetCellModel(selectedCellID);
            this.actionLineController.resetCellModel();
            this.customizationsController.deselectCell();
        } else {
            CellDTO cellDTO = this.engine.getSingleCellData(cellSubComponentController.cellIDProperty().get());
            this.actionLineController.showCellDetails(cellDTO);
            this.sheetGridController.showSelectedCellAndDependencies(cellDTO);
            this.customizationsController.setSelectedCell(cellDTO);
        }
    }
    
    public void updateCellValue(String cellToUpdate, String newValue) {
        try {
            this.engine.updateSingleCellData(cellToUpdate, newValue);
            SheetDTO sheetDTO = this.engine.getSheetAsDTO();
            CellDTO cellDTO = this.engine.getSingleCellData(cellToUpdate);
            this.sheetGridController.updateGridModel(sheetDTO.getCells());
            this.actionLineController.showCellDetails(cellDTO);
            this.sheetGridController.showSelectedCellAndDependencies(cellDTO);
            this.topSubComponentController.updateSheetVersion(sheetDTO.getVersion());
        } catch (RuntimeException e) {
            System.out.println("Error Updating Cell:\n" + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }
    
    public int getSheetVersions() {
        return this.engine.isSheetLoaded() ? this.engine.showVersions().getVersionChanges().size() : 0;
    }
    
    public void loadSheetVersion(int version) {
        SheetDTO sheetDTO = this.engine.getSheetVersionAsDTO(version);
        createReadonlyGrid(sheetDTO, " - version " + version);
    }
    
    public void openGridPopup(GridBuilder gridBuilder, String title, String sheetName) {
        try {
            // Create a new Stage (pop-up window)
            Stage popupStage = new Stage();
            popupStage.setTitle(sheetName + title);
            
            ScrollPane popupGrid = gridBuilder.build();
            popupGrid.getStyleClass().add("grid-popup");
            
            // Create a new scene for the pop-up window
            Scene popupScene = new Scene(popupGrid, 600, 400); // Set preferred width and height
            popupStage.setScene(popupScene);
            popupStage.getIcons().add(
                    new Image(Objects.requireNonNull(
                            Main.class.getResourceAsStream("/gui/main/view/style/shticellLogo.png"))));
            
            // Show the pop-up window
            popupStage.show();
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
    }
    
    public RangeDTO addNewRange(String rangeName, String from, String to) {
        try {
            return this.engine.addRange(rangeName, from + ".." + to);
        } catch (RuntimeException e) {
            this.rangesController.updateSaveErrorLabel(e.getMessage());
            return null;
        }
    }
    
    public boolean deleteRange(RangeDTO rangeToDelete) {
        try {
            this.engine.removeRange(rangeToDelete.getName());
            return true;
        } catch (RuntimeException e) {
            this.rangesController.updateDeleteErrorLabel(e.getMessage());
            return false;
        }
    }
    
    public void toggleSelectedRange(RangeDTO selectedRange, RangeDTO previousSelectedRange) {
        this.sheetGridController.toggleSelectedRange(selectedRange, previousSelectedRange);
    }
    
    public void updateColumnWidth(Integer newColumnWidth, int columnToUpdate) {
        GridPane gridPane = this.getSheetGrid();
        gridPane.getColumnConstraints().get(columnToUpdate).setMinWidth(newColumnWidth);
        gridPane.getColumnConstraints().get(columnToUpdate).setPrefWidth(newColumnWidth);
        gridPane.getColumnConstraints().get(columnToUpdate).setMaxWidth(newColumnWidth);
        
    }
    
    public void updateRowHeight(Integer newRowHeight, int rowToUpdate) {
        GridPane gridPane = this.getSheetGrid();
        gridPane.getRowConstraints().get(rowToUpdate).setMinHeight(newRowHeight);
        gridPane.getRowConstraints().get(rowToUpdate).setPrefHeight(newRowHeight);
        gridPane.getRowConstraints().get(rowToUpdate).setMaxHeight(newRowHeight);
    }
    
    private GridPane getSheetGrid() {
        BorderPane root = (BorderPane) this.primaryStage.getScene().getRoot();
        ScrollPane scrollPane = (ScrollPane) root.getCenter();
        return (GridPane) scrollPane.getContent();
    }
    
    
    public void setSelectedColumn(String columnName) {
        int columnIndex = (columnName.charAt(0) - 'A') + 1 ;
        int currentPrefWidth = (int)this.getSheetGrid().getColumnConstraints().get(columnIndex).getPrefWidth();
        this.customizationsController.setSelectedColumn(columnName, currentPrefWidth);
    }
    
    public void setSelectedRow(String rowName) {
        int rowIndex = Integer.parseInt(rowName);
        int currentPrefHeight = (int)this.getSheetGrid().getRowConstraints().get(rowIndex).getPrefHeight();
        this.customizationsController.setSelectedRow(rowIndex, currentPrefHeight);
    }
    
    public void setColumnTextAlignment(String columnName, String alignment) {
        this.cellSubComponentControllerMap.forEach((cellID, cellController) -> {
            if (cellID.contains(columnName)) {
                cellController.setAlignment(alignment);
            }
        });
    }
    
    public void setCellStyle(String cellID, Color backgroundColor, Color textColor) {
        this.cellSubComponentControllerMap.get(cellID).setCellStyle(backgroundColor, textColor);
        this.engine.updateCellStyle(cellID, backgroundColor, textColor);
    }
    
    public boolean sortRange(String rangeToSort, List<String> columnsToSortBy) {
        try {
            SheetDTO sortedSheet = this.engine.sortRangeOfCells(rangeToSort, columnsToSortBy);
            createReadonlyGrid(sortedSheet, " - Sorted");
            return true;
        } catch (ClassCastException e) {
            this.commandsController.updateSortErrorLabel("Cannot sort by non-numeric column");
            return false;
        } catch (RuntimeException e) {
            this.commandsController.updateSortErrorLabel(e.getMessage());
            return false;
        }
    }
    
    private void createReadonlyGrid(SheetDTO sortedSheet, String popupName) {
        GridBuilder gridBuilder = new GridBuilder(
                sortedSheet.getLayout().getRow(),
                sortedSheet.getLayout().getColumn(),
                sortedSheet.getLayout().getRowHeight(),
                sortedSheet.getLayout().getColumnWidth());
        
        this.openGridPopup(gridBuilder, popupName, sortedSheet.getSheetName());
        SheetGridController gridPopupController = gridBuilder.getSheetGridController();
        gridPopupController.initializeGridModel(sortedSheet.getCells());
        
        gridPopupController.getCellsControllers().forEach((cellID, cellController) -> {
            cellController.addOldVersionStyleClass();
        });
    }
}
