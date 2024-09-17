package gui.grid;

import component.cell.api.CellType;
import dto.CellDTO;
import dto.RangeDTO;
import gui.cell.CellModel;
import gui.cell.CellSubComponentController;
import gui.cell.DependenciesCellModel;
import gui.main.view.MainViewController;
import javafx.scene.control.Button;
import logic.function.returnable.api.Returnable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SheetGridController {

    private GridModel gridModel;
    private MainViewController mainViewController;
    private DependenciesCellModel dependenciesCellModel = new CellModel();
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
            this.gridModel.getCellValueProperty(cellID).set(effectiveValueFormatter(returnable));
        });
    }
    
    private String effectiveValueFormatter(Returnable effectiveValue){
        CellType type = effectiveValue.getCellType();
        String valueToPrint = effectiveValue.getValue().toString();
        if (type.equals(CellType.BOOLEAN)) {
            valueToPrint = booleanFormatter(valueToPrint);
        } else if (type.equals(CellType.NUMERIC)) {
            valueToPrint = numberFormatter(valueToPrint);
        }
        
        return valueToPrint;
    }
    
    private String numberFormatter(String valueToPrint) {
        try{
            double  number = Double.parseDouble(valueToPrint);
            DecimalFormat formatter = new DecimalFormat("#,###.##");
            formatter.setRoundingMode(RoundingMode.DOWN);
            return formatter.format(number);
        } catch (Exception ignored) {
            return valueToPrint;
        }
    }
    
    public String booleanFormatter(String valueToPrint) {
        return valueToPrint.toUpperCase();
    }
    
    
    public void showSelectedCellAndDependencies(CellDTO cellDTO) {
        String previousSelected = this.dependenciesCellModel.getSelectedCellProperty().get();
        
        this.updateSelectedCell(cellDTO.getCellId(), previousSelected);
        this.updateDependingOn(cellDTO);
        this.updateInfluencingOn(cellDTO);
    }
    
    private void updateInfluencingOn(CellDTO cellDTO) {
        this.dependenciesCellModel.getInfluencingOn().forEach((currentInfluence) -> {
            this.cellsControllers.get(currentInfluence).deselect("influencing-cell");
        });
        
        if(cellDTO.getInfluencingOn() != null){
            cellDTO.getInfluencingOn().forEach((currentInfluence) -> {
                this.cellsControllers.get(currentInfluence).select("influencing-cell");
            });
            this.dependenciesCellModel.setInfluencingOn(cellDTO.getInfluencingOn());
        }
    }
    
    private void updateDependingOn(CellDTO cellDTO) {
        this.dependenciesCellModel.getDependingOn().forEach((currentDependency) -> {
            this.cellsControllers.get(currentDependency).deselect("depending-cell");
        });
        
        if(cellDTO.getDependingOn() != null){
            cellDTO.getDependingOn().forEach((currentDependingOn) -> {
                this.cellsControllers.get(currentDependingOn).select("depending-cell");
            });
            this.dependenciesCellModel.setDependingOn(cellDTO.getDependingOn());
        }
    }
    
    private void updateSelectedCell(String CellID, String previousSelected) {
        if (previousSelected != null) {
            this.cellsControllers.get(previousSelected).deselect("selected-cell");
        }
        this.cellsControllers.get(CellID).select("selected-cell");
        this.dependenciesCellModel.setSelectedCell(CellID);
    }
    
    public void toggleSelectedRange(RangeDTO selectedRange, RangeDTO previousSelectedRange) {
        if (previousSelectedRange != null) {
            previousSelectedRange.getCells().forEach((cellID) -> {
                this.cellsControllers.get(cellID).deselect("selected-range");
            });
        }
        
        if (selectedRange != null) {
            selectedRange.getCells().forEach((cellID) -> {
                this.cellsControllers.get(cellID).select("selected-range");
            });
        }
    }
    
    public boolean isAlreadySelected(String cellID) {
        return this.dependenciesCellModel.isSelectedCell(cellID);
    }
    
    public void resetCellModel(String selectedCellID) {
        this.cellsControllers.get(selectedCellID).deselect("selected-cell");
        this.dependenciesCellModel.setSelectedCell(null);
        
        this.dependenciesCellModel.getDependingOn().forEach((currentDependency) -> {
            this.cellsControllers.get(currentDependency).deselect("depending-cell");
        });
        
        this.dependenciesCellModel.getInfluencingOn().forEach((currentInfluence) -> {
            this.cellsControllers.get(currentInfluence).deselect("influencing-cell");
        });
    }
}
