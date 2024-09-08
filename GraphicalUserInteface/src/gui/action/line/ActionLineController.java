package gui.action.line;

import gui.top.TopSubComponentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ActionLineController {
    private TopSubComponentController topSubComponentController;
    
    @FXML
    private Label cellIDLabel;
    
    @FXML
    private Label lastUpdatedCellValueVersionLabel;
    
    @FXML
    private TextField originalValueTextField;
    
    @FXML
    private Button updateValueButton;
    
    @FXML
    private void onUpdateValuePressed(ActionEvent event) {
        lastUpdatedCellValueVersionLabel.setText("next version");
        this.topSubComponentController.changeSomething();
    }
    
    public void setTopSubComponentController(TopSubComponentController topSubComponentController) {
        this.topSubComponentController = topSubComponentController;
    }
    
}
