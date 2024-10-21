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
    private StringProperty sheetVersionProperty;
    private MainEditorController mainViewController;
    private BooleanProperty isFileLoadedProperty;

    public TopSubComponentController() {
        this.sheetNameProperty = new SimpleStringProperty("Sheet Name");
        this.sheetVersionProperty = new SimpleStringProperty("-");
        this.isFileLoadedProperty = new SimpleBooleanProperty(false);
    }
    
    @FXML
    public void initialize() {
        if (this.actionLineController != null) {
            this.actionLineController.setTopSubComponentController(this);
        }
        this.versionsChoiceBox.getItems().add("Select Version");
        this.versionsChoiceBox.getSelectionModel().select("Select Version");
        
        this.sheetNameTitledPane.textProperty().bind(this.sheetNameProperty);
        this.versionsChoiceBox.disableProperty().bind(this.isFileLoadedProperty.not());

        this.versionsChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                
            boolean isGettingLastVersion = false;
            if (newValue != null && !newValue.contains("Select Version")) {
                if (newValue.equals(this.versionsChoiceBox.getItems().getLast())) {
                    isGettingLastVersion = true;
                }
                
                this.mainViewController.loadSheetVersion(
                        Integer.parseInt(this.versionsChoiceBox.getValue().substring(8)),
                        isGettingLastVersion);
            }
                this.versionsChoiceBox.getSelectionModel().selectFirst();
        });
    }
    
    @FXML
    public void onVersionMenuClicked(MouseEvent mouseEvent) {
        mouseEvent.consume();
        
        this.mainViewController.getLatestVersionNumber();
    }
    
    public void updateVersionsChoiceBox(int numOfVersions) {

        for (int i = 1; i <= numOfVersions; i++) {
            if(!this.versionsChoiceBox.getItems().contains("version " + i)) {
                this.versionsChoiceBox.getItems().add("version " + i);
                this.versionsChoiceBox.styleProperty().set("-fx-mark-color: transparent");
            }
        }
        this.versionsChoiceBox.show();
    }
    
    public void setMainController(MainEditorController mainViewController) {
        this.mainViewController = mainViewController;
    }
    
    public ActionLineController getActionLIneController() {
        return this.actionLineController;
    }
    
    public void setSheetNameAndVersion(String sheetName, int sheetVersion) {
        this.sheetNameProperty.set(sheetName);
        this.sheetVersionProperty.set(String.valueOf(sheetVersion));
        this.versionsChoiceBox.getItems().clear();
        this.versionsChoiceBox.getItems().add("Select Version");
        this.versionsChoiceBox.getSelectionModel().select("Select Version");
    }
    
    public void updateSheetVersion(int version) {
        this.sheetVersionProperty.set(String.valueOf(version));
    }

    public void bindFileNotLoaded(BooleanProperty isFileLoaded) {
        this.isFileLoadedProperty.bind(isFileLoaded.not());
    }
}
