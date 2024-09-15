package gui.main.view;

import dto.CellDTO;
import dto.SheetDTO;
import gui.Main;
import gui.action.line.ActionLineController;
import gui.cell.CellSubComponentController;
import gui.grid.GridBuilder;
import gui.grid.SheetGridController;
import gui.top.TopSubComponentController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.Engine;
import logic.EngineImpl;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class MainViewController {
    @FXML private TopSubComponentController topSubComponentController;
    private ActionLineController actionLineController;
    private SheetGridController sheetGridController;
    private Map<String, CellSubComponentController> cellSubComponentControllerMap;
    
    private Engine engine;
    private Stage primaryStage;
    
    @FXML
    public void initialize() {
        this.engine = new EngineImpl();
        
        if (this.topSubComponentController != null) {
            this.topSubComponentController.setMainController(this);
            this.setActionLineController(this.topSubComponentController.getActionLIneController());
        }
        
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
            GridBuilder gridBuilder = new GridBuilder(sheetDTO.getLayout().getRow(),
                                                      sheetDTO.getLayout().getColumn(),
                                                      sheetDTO.getLayout().getRowHeight(),
                                                      sheetDTO.getLayout().getColumnWidth());
            
            BorderPane root = (BorderPane)this.primaryStage.getScene().getRoot();
            root.setCenter(gridBuilder.build());
            this.setSheetGridController(gridBuilder.getSheetGridController());
            this.setCellSubComponentControllerMap(this.sheetGridController.getCellsControllers());
            this.sheetGridController.initializeGridModel(sheetDTO.getCells());
            
            this.actionLineController.toggleFileLoadedProperty();
            this.actionLineController.resetCellModel();
            this.topSubComponentController.setSheetNameAndVersion(sheetDTO.getSheetName(), sheetDTO.getVersion());
            
        } catch (RuntimeException | IOException e) {
            // Create error popup dialog
            System.out.println("Error Loading File:\n" + e.getMessage() + "\n");
        }
    }
    
    public void showCellDetails(CellSubComponentController cellSubComponentController) {
        CellDTO cellDTO = this.engine.getSingleCellData(cellSubComponentController.cellIDProperty().get());
        this.actionLineController.showCellDetails(cellDTO);
        this.sheetGridController.showSelectedCellAndDependencies(cellDTO); // Maybe send cellController as parameter?
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
        GridBuilder gridBuilder = new GridBuilder(
                sheetDTO.getLayout().getRow(),
                sheetDTO.getLayout().getColumn(),
                sheetDTO.getLayout().getRowHeight(),
                sheetDTO.getLayout().getColumnWidth());
        
        this.openGridPopup(gridBuilder, version, sheetDTO.getSheetName());
        SheetGridController gridPopupController = gridBuilder.getSheetGridController();
        gridPopupController.initializeGridModel(sheetDTO.getCells());
        
        gridPopupController.getCellsControllers().forEach((cellID, cellController) -> {
            cellController.addOldVersionStyleClass();
        });
    }
    
    public void openGridPopup(GridBuilder gridBuilder, int version, String sheetName) {
        try {
            // Create a new Stage (pop-up window)
            Stage popupStage = new Stage();
            popupStage.setTitle(sheetName + " - version " + version);
            
            ScrollPane popupGrid = gridBuilder.build();
            popupGrid.getStyleClass().add("grid-popup");
            
            // Create a new scene for the pop-up window
            Scene popupScene = new Scene(popupGrid, 600, 400); // Set preferred width and height
            popupStage.setScene(popupScene);
            popupStage.getIcons().add(
                    new Image(Objects.requireNonNull(
                            Main.class.getResourceAsStream("/gui/main/view/letter-s.png"))));
            
            // Show the pop-up window
            popupStage.show();
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
    }
}
