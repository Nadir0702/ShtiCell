package client.gui.home.sheet.table;

import client.gui.home.main.view.HomeViewController;
import client.task.SheetTableRefresher;
import dto.SheetMetaDataDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static client.gui.util.Constants.REFRESH_RATE;

public class SheetsTableController implements Closeable {
    
    @FXML private TableColumn<SheetTableEntry, String> permissionsColumn;
    @FXML private TableColumn<SheetTableEntry, String> sheetNameColumn;
    @FXML private TableColumn<SheetTableEntry, String> sheetSizeColumn;
    @FXML private TableColumn<SheetTableEntry, String> userNameColumn;
    @FXML private TableView<SheetTableEntry> sheetsTableView;
    
    private ObservableList<SheetTableEntry> availableSheets;
    private HomeViewController homeViewController;
    private TimerTask tableRefresher;
    private Timer timer;
    
    public SheetsTableController() {
        this.availableSheets = FXCollections.observableArrayList();
    }
    
    @FXML
    private void initialize() {
        this.initializeTableView();
    }
    
    private void initializeTableView() {
        this.permissionsColumn.setCellValueFactory(new PropertyValueFactory<>("permission"));
        this.sheetNameColumn.setCellValueFactory(new PropertyValueFactory<>("sheetName"));
        this.sheetSizeColumn.setCellValueFactory(new PropertyValueFactory<>("sheetSize"));
        this.userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        
        this.sheetsTableView.setItems(this.availableSheets);
    }
    
    public void setMainController(HomeViewController homeViewController) {
        this.homeViewController = homeViewController;
    }
    
    public void addSheetEntry(SheetMetaDataDTO sheetToAdd) {
        this.availableSheets.add(new SheetTableEntry(
                sheetToAdd.getOwnerName(),
                sheetToAdd.getSheetName(),
                sheetToAdd.getNumberOfRows() + "x" + sheetToAdd.getNumberOfColumns(),
                sheetToAdd.getPermission()));
    }
    
    public void startListRefresher() {
        this.tableRefresher = new SheetTableRefresher(this::updateSheetsTable);
        timer = new Timer();
        timer.schedule(this.tableRefresher, 10000, REFRESH_RATE);
    }
    
    private void updateSheetsTable(List<SheetMetaDataDTO> sheets) {
        Platform.runLater(() -> {
            availableSheets.clear();
            sheets.forEach(this::addSheetEntry);
        });
    }
    
    @Override
    public void close() throws IOException {
        this.sheetsTableView.getItems().clear();
        if (this.tableRefresher != null && timer != null) {
            this.tableRefresher.cancel();
            this.timer.cancel();
        }
    }
}
