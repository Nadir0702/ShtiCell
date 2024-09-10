package gui.main.view;

import dto.CellDTO;
import dto.SheetDTO;
import gui.action.line.ActionLineController;
import gui.cell.CellSubComponentController;
import gui.grid.GridBuilder;
import gui.grid.SheetGridController;
import gui.top.TopSubComponentController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.Engine;
import logic.EngineImpl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

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
    
    public void setCellSubComponentControllerMap(Map<String, CellSubComponentController> cellSubComponentControllerMap) {
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
            this.engine.LoadData(absolutePath);
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
}
