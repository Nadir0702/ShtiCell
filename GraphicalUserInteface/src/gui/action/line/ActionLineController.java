package gui.action.line;

import dto.CellDTO;
import gui.cell.ActionLineCellModel;
import gui.cell.CellModel;
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
    
    @FXML private Label cellIDLabel;
    @FXML private Label lastUpdatedCellValueVersionLabel;
    @FXML private TextField originalValueTextField;
    @FXML private Button updateValueButton;
    
    private TopSubComponentController topSubComponentController;
    private MainViewController mainViewController;
    private ActionLineCellModel actionLineCellModel;
    private BooleanProperty FileNotLoaded;
    
    public ActionLineController() {
        actionLineCellModel = new CellModel();
        FileNotLoaded = new SimpleBooleanProperty(true);
       
    }
    
    @FXML
    private void initialize() {
        updateValueButton.disableProperty().bind(Bindings.or(FileNotLoaded, originalValueTextField.textProperty().isEmpty()));
        actionLineCellModel.bind(cellIDLabel.textProperty(), originalValueTextField.textProperty(), lastUpdatedCellValueVersionLabel.textProperty());
    }
    
    public void toggleFileLoadedProperty() {
        FileNotLoaded.set(false);
    }
    
    @FXML
    private void onUpdateValuePressed(ActionEvent event) {
    
    }
    
    public void setTopSubComponentController(TopSubComponentController topSubComponentController) {
        this.topSubComponentController = topSubComponentController;
    }
    
    public void setMainController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
    
    public void showCellDetails(CellDTO cellDTO) {
        this.actionLineCellModel.getCellIDProperty().set(cellDTO.getCellId());
        this.actionLineCellModel.getOriginalValueProperty().set(cellDTO.getOriginalValue());
        this.actionLineCellModel.getLastUpdatedVersionProperty().set(String.valueOf(cellDTO.getVersion()));
    }
}
