package gui.top;

import gui.action.line.ActionLineController;
import gui.main.view.MainViewController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;

public class TopSubComponentController {
    
    @FXML private ActionLineController actionLineController;
    @FXML private ChoiceBox<String> versionsChoiceBox;
    @FXML private TextField filePathTextField;
    @FXML private TitledPane sheetNameTitledPane;
    @FXML private MenuButton themeMenuButton;
    
    private StringProperty filePathProperty;
    private StringProperty sheetNameProperty;
    private StringProperty sheetVersionProperty;
    private MainViewController mainViewController;
    
    public TopSubComponentController() {
        this.filePathProperty = new SimpleStringProperty("path/to/your/xml/file/fileName.xml");
        this.sheetNameProperty = new SimpleStringProperty("Sheet Name");
        this.sheetVersionProperty = new SimpleStringProperty("-");
    }
    
    @FXML
    public void initialize() {
        if (this.actionLineController != null) {
            this.actionLineController.setTopSubComponentController(this);
        }
        this.versionsChoiceBox.getItems().add("Latest Version");
        this.versionsChoiceBox.getSelectionModel().select("Latest Version");
        this.sheetVersionProperty.addListener((observable, oldValue, newValue) -> {
            this.versionsChoiceBox.getItems().add("Latest Version (" + this.sheetVersionProperty.get() + ")");
            this.versionsChoiceBox.getSelectionModel().select("Latest Version (" + this.sheetVersionProperty.get() + ")");
        });
        
        this.versionsChoiceBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.versionsChoiceBox.getItems().add("Latest Version (" + this.sheetVersionProperty.get() + ")");
                this.versionsChoiceBox.getSelectionModel().select("Latest Version (" + this.sheetVersionProperty.get() + ")");
            }
        });
        
        this.filePathTextField.textProperty().bind(this.filePathProperty);
        this.sheetNameTitledPane.textProperty().bind(this.sheetNameProperty);
        this.versionsChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.contains("Latest")) {
                this.mainViewController.loadSheetVersion(
                        Integer.parseInt(this.versionsChoiceBox.getValue().substring(8)));
            }
        });
        
    }
    
    @FXML
    private void onLoadXMLPressed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Sheet file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = this.mainViewController.getFilePath(fileChooser);
        if (selectedFile == null) {
            return;
        }
        
        String absolutePath = selectedFile.getAbsolutePath();
        this.mainViewController.loadNewSheetFromXML(absolutePath);
        this.filePathProperty.set(absolutePath);
    }
    
    @FXML
    public void onVersionMenuClicked(MouseEvent mouseEvent) {
        mouseEvent.consume();
        int numOfVersions = this.mainViewController.getSheetVersions();
        
        this.versionsChoiceBox.getItems().clear();
        for (int i = 1; i <= numOfVersions; i++) {
            this.versionsChoiceBox.getItems().add("version " + i);
        }
        
        this.versionsChoiceBox.show();
    }
    
    @FXML
    private void onThemeChanged(ActionEvent event) {
    
    }
    
    public void setMainController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
    
    public ActionLineController getActionLIneController() {
        return this.actionLineController;
    }
    
    public void setSheetNameAndVersion(String sheetName, int sheetVersion) {
        this.sheetNameProperty.set(sheetName);
        this.sheetVersionProperty.set(String.valueOf(sheetVersion));
        this.versionsChoiceBox.getItems().clear();
    }
    
    public void updateSheetVersion(int version) {
        this.sheetVersionProperty.set(String.valueOf(version));
    }
}
