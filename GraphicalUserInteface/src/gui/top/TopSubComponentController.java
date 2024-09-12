package gui.top;

import gui.action.line.ActionLineController;
import gui.main.view.MainViewController;
import javafx.beans.binding.Bindings;
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
        this.sheetVersionProperty = new SimpleStringProperty("-");
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
    public void onVersionMenuClicked(MouseEvent mouseEvent) {
        int numOfVersions = this.mainViewController.getSheetVersions();
        int numOfMenuItems = this.currentVersionMenuButton.getItems().size();
        
        if (numOfVersions > numOfMenuItems) {
            for (int i = numOfMenuItems; i < numOfVersions; i++) {
                MenuItem menuItem = new MenuItem("Version " + (i + 1));
                menuItem.setOnAction(this::onVersionChanged);
                this.currentVersionMenuButton.getItems().add(menuItem);
            }
        }
    }
    
    private void onVersionChanged(ActionEvent mouseEvent) {
        MenuItem menuItem = (MenuItem) mouseEvent.getSource();
        this.mainViewController.loadSheetVersion(
                Integer.parseInt(menuItem.getText().substring("Version ".length())));
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
        this.currentVersionMenuButton.getItems().clear();
    }
    
    public void updateSheetVersion(int version) {
        this.sheetVersionProperty.set(String.valueOf(version));
    }
}
