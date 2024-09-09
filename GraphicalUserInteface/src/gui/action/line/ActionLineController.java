package gui.action.line;

import gui.cell.CellSubComponentController;
import gui.main.view.MainViewController;
import gui.top.TopSubComponentController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ActionLineController {
    private TopSubComponentController topSubComponentController;
    
    @FXML private Label cellIDLabel;
    @FXML private Label lastUpdatedCellValueVersionLabel;
    @FXML private TextField originalValueTextField;
    @FXML private Button updateValueButton;
    
    private StringProperty originalValueProperty;
    private BooleanProperty FileNotLoaded;
    private StringProperty cellIDProperty;
    private MainViewController mainViewController;
    
    public ActionLineController() {
        FileNotLoaded = new SimpleBooleanProperty(true);
        cellIDProperty = new SimpleStringProperty("Cell ID ");
        originalValueProperty = new SimpleStringProperty();
    }
    
    @FXML
    private void initialize() {
        updateValueButton.disableProperty().bind(Bindings.or(FileNotLoaded, originalValueTextField.textProperty().isEmpty()));
        cellIDLabel.textProperty().bind(cellIDProperty);
        originalValueTextField.textProperty().bind(originalValueProperty);
    }
    
    public void toggleFileLoadedProperty() {
        FileNotLoaded.set(!FileNotLoaded.getValue());
    }
    
    @FXML
    private void onUpdateValuePressed(ActionEvent event) {
        lastUpdatedCellValueVersionLabel.setText("next version");
    }
    
    public void setTopSubComponentController(TopSubComponentController topSubComponentController) {
        this.topSubComponentController = topSubComponentController;
    }
    
    public void setMainController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
    
    public void showCellDetails(CellSubComponentController cellSubComponentController) {
        cellIDProperty.set("Cell ID " + cellSubComponentController.cellIDProperty().get());
        originalValueProperty.set(cellSubComponentController.getCellValueProperty().get());
    }
}
