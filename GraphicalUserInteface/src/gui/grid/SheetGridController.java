package gui.grid;

import dto.CellDTO;
import gui.cell.CellModel;
import gui.cell.CellSubComponentController;
import gui.cell.DependenciesCellModel;
import gui.main.view.MainViewController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import logic.function.returnable.api.Returnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SheetGridController {

    private DependenciesCellModel dependenciesCellModel = new CellModel();
    private GridModel gridModel;
    private MainViewController mainViewController;
    private final List<Button> columnHeaders = new ArrayList<>();
    private final List<Button> rowHeaders = new ArrayList<>();
    private final Map<String, CellSubComponentController> cellsControllers = new HashMap<>();
    
    public void addColumnHeader(Button button) {
        this.columnHeaders.add(button);
    }
    
    public void addRowHeader(Button button) {
        this.rowHeaders.add(button);
    }
    
    public void addCellController(String cellID ,CellSubComponentController controller) {
        this.cellsControllers.put(cellID, controller);
    }
    
    public void setMainController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
    
    public Map<String, CellSubComponentController> getCellsControllers() {
        return this.cellsControllers;
    }
    
    public void initializeGridModel(Map<String, Returnable> cells) {
        this.gridModel = new GridModel(cellsControllers);
        this.updateGridModel(cells);
    }
    
    public void updateGridModel(Map<String, Returnable> cells) {
        cells.forEach((cellID, returnable) -> {
            this.gridModel.getCellValueProperty(cellID).set(returnable.getValue().toString());
        });
    }
    
    public void showSelectedCellAndDependencies(CellDTO cellDTO) {
        String previousSelected = this.dependenciesCellModel.getSelectedCellProperty().get();
        
        if (previousSelected != null) {
            this.cellsControllers.get(previousSelected).deselect("selected-cell");
        }
        
        this.dependenciesCellModel.getDependingOn().forEach((currentDependency) -> {
            this.cellsControllers.get(currentDependency).deselect("depending-cell");
        });
        
        this.dependenciesCellModel.getInfluencingOn().forEach((currentInfluence) -> {
            this.cellsControllers.get(currentInfluence).deselect("influencing-cell");
        });
        
        this.cellsControllers.get(cellDTO.getCellId()).select("selected-cell");
        this.dependenciesCellModel.setSelectedCell(cellDTO.getCellId());
        
        if(cellDTO.getDependingOn() != null){
            cellDTO.getDependingOn().forEach((currentDependingOn) -> {
                this.cellsControllers.get(currentDependingOn).select("depending-cell");
            });
            this.dependenciesCellModel.setDependingOn(cellDTO.getDependingOn());
        }
        
        if(cellDTO.getInfluencingOn() != null){
            cellDTO.getInfluencingOn().forEach((currentInfluence) -> {
                this.cellsControllers.get(currentInfluence).select("influencing-cell");
            });
            this.dependenciesCellModel.setInfluencingOn(cellDTO.getInfluencingOn());
        }
        
    }
}
