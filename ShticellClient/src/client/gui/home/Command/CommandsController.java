package client.gui.home.Command;

import client.gui.home.main.view.HomeViewController;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CommandsController {
    
    @FXML private TableView<?> recievedRequestsTableView;
    @FXML private TableColumn<?, ?> senderColumn;
    @FXML private TableColumn<?, ?> sheetNameColumn;
    @FXML private TableColumn<?, ?> permissionColumn;
    @FXML private ChoiceBox<String> newPermissionChoiceBox;
    @FXML private TextField sheetNameTextField;
    @FXML private Button viewSheetButton;
    @FXML private Button sendPermissionRequestButton;
    @FXML private Button acceptRequestButton;
    @FXML private Button declineRequestButton;
    
    private HomeViewController homeViewController;
    
    @FXML
    private void initialize() {
        this.newPermissionChoiceBox.getItems().add("Permission Type");
        this.newPermissionChoiceBox.getItems().add("Reader");
        this.newPermissionChoiceBox.getItems().add("Writer");
        this.newPermissionChoiceBox.getItems().add("None");
        this.newPermissionChoiceBox.getSelectionModel().selectFirst();
        
        this.sendPermissionRequestButton.disableProperty().bind(
                Bindings.or(this.sheetNameTextField.textProperty().isEmpty(),
                        this.newPermissionChoiceBox.getSelectionModel()
                                .selectedItemProperty().isEqualTo("Permission Type"))
        );
    }
    
    @FXML
    void onAcceptRequestClicked(ActionEvent event) {
    
    }
    
    @FXML
    void onDeclineRequestClicked(ActionEvent event) {
    
    }
    
    @FXML
    void onSendButtonClicked(ActionEvent event) {
        this.homeViewController.sendNewPermissionRequest(
                this.sheetNameTextField.getText(),
                this.newPermissionChoiceBox.getSelectionModel().getSelectedItem());
    }
    
    @FXML
    void onViewSheetClicked(ActionEvent event) {
    
    }
    
    public void setMainController(HomeViewController homeViewController) {
        this.homeViewController = homeViewController;
    }
}
