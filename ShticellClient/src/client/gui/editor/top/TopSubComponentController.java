package client.gui.editor.top;

import client.gui.editor.action.line.ActionLineController;
import client.gui.editor.main.view.MainEditorController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class TopSubComponentController {
    
    @FXML private ActionLineController actionLineController;
    @FXML private ChoiceBox<String> versionsChoiceBox;
    @FXML private TitledPane sheetNameTitledPane;
    
    private StringProperty sheetNameProperty;
    private MainEditorController mainViewController;
    private int latestVersion;

    public TopSubComponentController() {
        this.sheetNameProperty = new SimpleStringProperty("Sheet Name");
        this.latestVersion = 1;
    }
    
    @FXML
    public void initialize() {
        if (this.actionLineController != null) {
            this.actionLineController.setTopSubComponentController(this);
        }
        
        this.sheetNameTitledPane.textProperty().bind(this.sheetNameProperty);

        this.versionsChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    
                    if (newValue != null && oldValue != null && !newValue.equals(oldValue)) {
                        this.mainViewController.loadSheetVersion(
                                this.versionsChoiceBox.getSelectionModel().getSelectedIndex() + 1);
                    }
        });
    }
    
    @FXML
    public void onVersionMenuClicked(MouseEvent mouseEvent) {
        mouseEvent.consume();
        
        this.mainViewController.getLatestVersionNumber();
    }
    
    public void updateVersionsChoiceBox(int numOfVersions) {
        this.latestVersion = numOfVersions;
        String previousSelection = this.versionsChoiceBox.getSelectionModel().getSelectedItem();
        this.versionsChoiceBox.getItems().clear();
        for (int i = 1; i <= numOfVersions; i++) {
            this.versionsChoiceBox.getItems().add("Version " + i + "/" + numOfVersions);
            if (previousSelection.contains("Version " + i)) {
                this.versionsChoiceBox.getSelectionModel().select(i - 1);
            }
        }
        this.versionsChoiceBox.show();
    }
    
    public void setSheetNameAndVersion(String sheetName, int sheetVersion) {
        if (this.latestVersion < sheetVersion) {
            this.latestVersion = sheetVersion;
        }
        
        this.sheetNameProperty.set(sheetName);
        this.versionsChoiceBox.getItems().clear();
        this.versionsChoiceBox.getItems().add("Version " + sheetVersion + "/" + this.latestVersion);
        this.versionsChoiceBox.getSelectionModel().select(
                "Version " + sheetVersion + "/" + this.latestVersion);
    }
    
    public void setMainController(MainEditorController mainViewController) {
        this.mainViewController = mainViewController;
    }
    
    public ActionLineController getActionLIneController() {
        return this.actionLineController;
    }
}
