package gui.grid;

import gui.cell.CellSubComponentController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Map;

public class GridModel {
    
    private final Map<String, StringProperty> cellsProperties;
//    private StringProperty selectedCellProperty;
    
    public GridModel(Map<String, CellSubComponentController> cellsControllers) {
        this.cellsProperties = new HashMap<>();
//        this.selectedCellProperty = new SimpleStringProperty();
        
        cellsControllers.forEach((cellID, cellController) -> {
            StringProperty currentCellProperty = new SimpleStringProperty();
            this.cellsProperties.put(cellID, currentCellProperty);
            cellController.getCellValueProperty().bind(currentCellProperty);
        });
    }
    
//    public StringProperty getSelectedCellProperty() {
//        return this.selectedCellProperty;
//    }
    
    public StringProperty getCellValueProperty(String cellID) {
        return this.cellsProperties.get(cellID);
    }
}
