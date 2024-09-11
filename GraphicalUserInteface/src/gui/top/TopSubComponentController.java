package gui.top;

import gui.action.line.ActionLineController;
import gui.main.view.MainViewController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.FileChooser;

import java.io.File;

public class TopSubComponentController {
    
    @FXML private ActionLineController actionLineController;
    @FXML private MenuButton currentVersionMenuButton;
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
        this.sheetVersionProperty = new SimpleStringProperty("");
    }
    
    @FXML
    public void initialize() {
        if (this.actionLineController != null) {
            this.actionLineController.setTopSubComponentController(this);
        }
        
        this.filePathTextField.textProperty().bind(this.filePathProperty);
        this.sheetNameTitledPane.textProperty().bind(this.sheetNameProperty);
        this.currentVersionMenuButton.textProperty().bind(
                Bindings.concat("Version ", this.sheetVersionProperty));
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
    private void onThemeChanged(ActionEvent event) {
    
    }
    
    @FXML
    private void onVersionChanged(ActionEvent event) {
    
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
    }
    
    public void updateSheetVersion(int version) {
        this.sheetVersionProperty.set(String.valueOf(version));
    }
}
