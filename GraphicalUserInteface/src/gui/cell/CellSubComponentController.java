package gui.cell;

import gui.main.view.MainViewController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class CellSubComponentController {
    
    @FXML
    private Label cellComponent;
    private MainViewController mainViewController;
    private StringProperty cellID;
    
    @FXML
    private void initialize() {
        this.cellID = new SimpleStringProperty();
    }
    
    @FXML
    void onCellPressed(MouseEvent event) {
        this.mainViewController.showCellDetails(this);
    }
    
    public StringProperty cellIDProperty() {
        return this.cellID;
    }
    
    public StringProperty getCellValueProperty() {
        return this.cellComponent.textProperty();
    }
    
    public void setMainController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
    
    public void deselect(String className) {
        this.cellComponent.getStyleClass().remove(className);
    }
    
    public void select(String className) {
        this.cellComponent.getStyleClass().add(className);
    }
}
