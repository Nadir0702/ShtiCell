package client.gui.editor.grid;

import client.gui.editor.main.view.MainEditorController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class BackButtonLayerController {
    
    private MainEditorController mainEditorController;
    
    @FXML
    void onBackButtonClicked(ActionEvent event) {
    
    }
    
    public void setMainController(MainEditorController mainEditorController) {
        this.mainEditorController = mainEditorController;
    }
}
