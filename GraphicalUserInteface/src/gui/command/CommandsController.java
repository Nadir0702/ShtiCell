package gui.command;

import gui.main.view.MainViewController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class CommandsController {
    
    @FXML private TextField bottomLeftBoundaryTextField;
    @FXML private TextField topRightBoundaryTextField;
    @FXML private ChoiceBox<String> filterColumnChoiceBox;
    @FXML private ChoiceBox<String> filterElementChoiceBox;
    @FXML private ChoiceBox<String> sortColumnChoiceBox;
    @FXML private Button sortButton;
    @FXML private Button filterButton;
    @FXML private Button addColumnSortButton;
    
    private List<ChoiceBox<String>> additionalColumnsToSortBy;
    
    private MainViewController mainViewController;
    private BooleanProperty isFileLoadedProperty;
    
    public CommandsController() {
        this.isFileLoadedProperty = new SimpleBooleanProperty(false);
        this.additionalColumnsToSortBy = new ArrayList<>();
    }
    
    @FXML private void initialize() {
        this.bottomLeftBoundaryTextField.disableProperty().bind(this.isFileLoadedProperty.not());
        this.topRightBoundaryTextField.disableProperty().bind(this.isFileLoadedProperty.not());
        this.sortColumnChoiceBox.getItems().addAll("Select Column", "C", "D", "E");
        this.sortColumnChoiceBox.getSelectionModel().selectFirst();
        
        this.sortColumnChoiceBox.disableProperty().bind(
                Bindings.or(this.isFileLoadedProperty.not(),
                            Bindings.or(this.bottomLeftBoundaryTextField.textProperty().isEmpty(),
                                    this.topRightBoundaryTextField.textProperty().isEmpty())));
        
        this.addColumnSortButton.disableProperty().bind(this.sortColumnChoiceBox.disableProperty());
        
        
        this.filterColumnChoiceBox.disableProperty().bind(
                Bindings.or(this.isFileLoadedProperty.not(),
                            Bindings.or(this.bottomLeftBoundaryTextField.textProperty().isEmpty(),
                                    this.topRightBoundaryTextField.textProperty().isEmpty())));
        
        
        this.filterElementChoiceBox.disableProperty().bind(
                Bindings.or(this.filterColumnChoiceBox.disableProperty(),
                        this.filterColumnChoiceBox.getSelectionModel().selectedItemProperty().isEqualTo("Select Item")));
        
        this.sortButton.disableProperty().bind(this.sortColumnChoiceBox.disableProperty());
        this.filterButton.disableProperty().bind(this.filterElementChoiceBox.disableProperty());
        
    }
    
    @FXML
    void onFilterButtonClicked(ActionEvent event) {
    
    }
    
    @FXML
    void onSortClicked(ActionEvent event) {
        if (!this.sortColumnChoiceBox.getSelectionModel().getSelectedItem().equals("Select Column")) {
            String rangeToSort = this.topRightBoundaryTextField.getText()
                    + ".." + this.bottomLeftBoundaryTextField.getText();
            List<String> columnsToSortBy = new ArrayList<>();
            columnsToSortBy.add(this.sortColumnChoiceBox.getSelectionModel().getSelectedItem());
            
            this.additionalColumnsToSortBy.forEach((column) -> {
                columnsToSortBy.add(column.getValue());
            });
            
            this.mainViewController.sortRange(rangeToSort, columnsToSortBy);
        }
    }
    
    @FXML
    void onAddColumnSortClicked(ActionEvent event) {
        ChoiceBox<String> columnChoiceBox = new ChoiceBox<>();
        columnChoiceBox.getItems().addAll("Select Column", "C", "D", "E");
        columnChoiceBox.getSelectionModel().selectFirst();
        this.additionalColumnsToSortBy.add(columnChoiceBox);
    }
    
    public void setMainController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
    
    public void bindFileNotLoaded(BooleanProperty isFileLoaded) {
        this.isFileLoadedProperty.bind(isFileLoaded.not());
    }
}
