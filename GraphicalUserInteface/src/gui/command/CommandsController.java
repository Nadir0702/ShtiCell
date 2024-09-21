package gui.command;

import gui.main.view.MainViewController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class CommandsController {
    
    @FXML private TextField bottomLeftBoundaryTextField;
    @FXML private TextField topRightBoundaryTextField;
    @FXML private TextField columnsToSortByTextField;
    @FXML private ChoiceBox<String> filterColumnChoiceBox;
    @FXML private ChoiceBox<String> filterElementChoiceBox;
    @FXML private Button sortButton;
    @FXML private Button filterButton;
    @FXML private Label sortErrorLabel;
    
    private List<ChoiceBox<String>> additionalColumnsToSortBy;
    
    private MainViewController mainViewController;
    private BooleanProperty isFileLoadedProperty;
    private StringProperty sortErrorProperty;
    
    public CommandsController() {
        this.isFileLoadedProperty = new SimpleBooleanProperty(false);
        this.sortErrorProperty = new SimpleStringProperty("");
        this.additionalColumnsToSortBy = new ArrayList<>();
    }
    
    @FXML private void initialize() {
        this.bottomLeftBoundaryTextField.disableProperty().bind(this.isFileLoadedProperty.not());
        this.topRightBoundaryTextField.disableProperty().bind(this.isFileLoadedProperty.not());
        this.sortErrorLabel.textProperty().bind(this.sortErrorProperty);
        
        this.columnsToSortByTextField.disableProperty().bind(
                Bindings.or(this.isFileLoadedProperty.not(),
                        Bindings.or(this.bottomLeftBoundaryTextField.textProperty().isEmpty(),
                                    this.topRightBoundaryTextField.textProperty().isEmpty())));
        
        this.filterColumnChoiceBox.disableProperty().bind(
                Bindings.or(this.isFileLoadedProperty.not(),
                            Bindings.or(this.bottomLeftBoundaryTextField.textProperty().isEmpty(),
                                        this.topRightBoundaryTextField.textProperty().isEmpty())));
        
        
        this.filterElementChoiceBox.disableProperty().bind(
                Bindings.or(this.filterColumnChoiceBox.disableProperty(),
                            this.filterColumnChoiceBox.getSelectionModel()
                                    .selectedItemProperty().isEqualTo("Select Item")));
        
        this.sortButton.disableProperty().bind(this.columnsToSortByTextField.textProperty().isEmpty());
        
        this.filterButton.disableProperty().bind(this.filterElementChoiceBox.disableProperty());
        
    }
    
    @FXML
    void onFilterButtonClicked(ActionEvent event) {
    
    }
    
    @FXML
    void onSortClicked(ActionEvent event) {
        String rangeToSort = this.topRightBoundaryTextField.getText()
                + ".." + this.bottomLeftBoundaryTextField.getText();
        List<String> columnsToSortBy = this.getColumnsToSortBy();
        if ( this.mainViewController.sortRange(rangeToSort, columnsToSortBy)) {
            this.sortErrorProperty.set("");
        }
    }
    
    private List<String> getColumnsToSortBy() {
        List<String> columnsToSortBy = new ArrayList<>();
        String[] columnsNames = this.columnsToSortByTextField.getText().split(";");
        for (String columnsName : columnsNames) {
            columnsToSortBy.add(columnsName.trim().toUpperCase());
        }
        
        return columnsToSortBy;
    }
    
    
    public void setMainController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
    
    public void bindFileNotLoaded(BooleanProperty isFileLoaded) {
        this.isFileLoadedProperty.bind(isFileLoaded.not());
    }
    
    public void resetController() {
        this.topRightBoundaryTextField.textProperty().set("");
        this.bottomLeftBoundaryTextField.textProperty().set("");
        this.columnsToSortByTextField.textProperty().set("");
        
        
    }
    
    public void updateSortErrorLabel(String message) {
        this.sortErrorProperty.set(message);
    }
}
