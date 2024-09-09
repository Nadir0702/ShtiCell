package gui.grid;

import gui.cell.CellSubComponentController;
import gui.main.view.MainViewController;
import javafx.scene.control.Button;
import logic.function.returnable.api.Returnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SheetGridController {

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
}
