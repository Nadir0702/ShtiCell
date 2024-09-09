package gui.top;

import com.sun.javafx.collections.ElementObservableListDecorator;
import gui.action.line.ActionLineController;
import gui.cell.CellSubComponentController;
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
    
    private StringProperty fileName;
    private StringProperty sheetName;
    private StringProperty sheetVersion;
    private MainViewController mainViewController;
    
    public TopSubComponentController() {
        this.fileName = new SimpleStringProperty("path/to/your/xml/file/filName.xml");
        this.sheetName = new SimpleStringProperty("Sheet Name");
        this.sheetVersion = new SimpleStringProperty("Version");
    }
    
    @FXML
    public void initialize() {
        if (this.actionLineController != null) {
            this.actionLineController.setTopSubComponentController(this);
        }
        
        this.filePathTextField.textProperty().bind(this.fileName);
        this.sheetNameTitledPane.textProperty().bind(Bindings.concat(this.sheetName, " ", this.sheetVersion));
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
        this.fileName.set(absolutePath);
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
        this.sheetName.set(sheetName);
        this.sheetVersion.set(String.valueOf(sheetVersion));
    }
    
}
