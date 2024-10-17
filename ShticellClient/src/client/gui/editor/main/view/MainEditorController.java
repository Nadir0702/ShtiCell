package client.gui.editor.main.view;

import client.gui.app.MainAppViewController;
import client.gui.exception.ExceptionWindowController;
import client.gui.util.Constants;
import client.gui.util.http.HttpClientUtil;
import client.main.Main;
import client.gui.editor.action.line.ActionLineController;
import client.gui.editor.cell.CellSubComponentController;
import client.gui.editor.command.CommandsController;
import client.gui.editor.customization.CustomizationController;
import client.gui.editor.graph.GraphType;
import client.gui.editor.grid.GridBuilder;
import client.gui.editor.grid.SheetGridController;
import client.gui.editor.ranges.RangesController;
import client.gui.editor.top.TopSubComponentController;
import dto.cell.CellDTO;
import dto.cell.ColoredCellDTO;
import dto.range.RangeDTO;
import dto.range.RangesDTO;
import dto.returnable.EffectiveValueDTO;
import dto.sheet.ColoredSheetDTO;
import dto.sheet.SheetAndCellDTO;
import dto.sheet.SheetAndRangesDTO;
import dto.sheet.SheetDTO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.engine.Engine;
import logic.engine.EngineImpl;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import user.User;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class MainEditorController {

    @FXML private TopSubComponentController topSubComponentController;
    @FXML private CustomizationController customizationsController;
    @FXML private CommandsController commandsController;
    @FXML private RangesController rangesController;
    
    private ActionLineController actionLineController;
    private SheetGridController sheetGridController;
    private Map<String, CellSubComponentController> cellSubComponentControllerMap;

    private BooleanProperty fileNotLoadedProperty;
    private Engine engine;
    private MainAppViewController mainAppController;
    
    public MainEditorController() {
        this.fileNotLoadedProperty = new SimpleBooleanProperty(true);
    }

    @FXML
    public void initialize() {
        this.engine = new EngineImpl(new User("User1"));

        if (this.topSubComponentController != null) {
            this.topSubComponentController.setMainController(this);
            this.setActionLineController(this.topSubComponentController.getActionLIneController());
        }

        if (this.rangesController != null) {
            this.rangesController.setMainController(this);
        }

        if (this.customizationsController != null) {
            this.customizationsController.setMainController(this);
        }

        if (this.commandsController != null) {
            this.commandsController.setMainController(this);
        }

        this.actionLineController.bindFileNotLoaded(this.fileNotLoadedProperty);
        this.rangesController.bindFileNotLoaded(this.fileNotLoadedProperty);
        this.customizationsController.bindFileNotLoaded(this.fileNotLoadedProperty);
        this.commandsController.bindFileNotLoaded(this.fileNotLoadedProperty);
        this.topSubComponentController.bindFileNotLoaded(this.fileNotLoadedProperty);
    }

    public void setActionLineController(ActionLineController actionLineController) {
        this.actionLineController = actionLineController;
        this.actionLineController.setMainController(this);
    }

    public void setSheetGridController(SheetGridController sheetGridController) {
        this.sheetGridController = sheetGridController;
        this.sheetGridController.setMainController(this);
    }

    public void setCellSubComponentControllerMap(
            Map<String, CellSubComponentController> cellSubComponentControllerMap) {

        this.cellSubComponentControllerMap = cellSubComponentControllerMap;
        this.cellSubComponentControllerMap.forEach((cellID, cellController) -> {
            cellController.setMainController(this);
        });
    }

    private void getSheetAndRanges() {
        Request request = new Request.Builder()
                .url(Constants.GET_SHEET_AND_RANGES)
                .build();
        
        HttpClientUtil.runAsync(request, new Callback() {
            
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        ExceptionWindowController.openExceptionPopup(
                                "Something went wrong: " + e.getMessage())
                );
            }
            
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    String responseBodyString = responseBody.string();
                    if (response.code() != 200) {
                        Platform.runLater(() ->
                                ExceptionWindowController.openExceptionPopup(
                                        "Something went wrong: " + responseBodyString)
                        );
                    } else {
                        SheetAndRangesDTO sheetAndRanges =
                                Constants.GSON_INSTANCE.fromJson(responseBodyString, SheetAndRangesDTO.class);
                     
                        Platform.runLater(() ->
                            initializeSheetLayoutAndControllers(
                                    sheetAndRanges.getSheetDTO(), sheetAndRanges.getRangesDTO())
                        );
                    }
                } catch (RuntimeException e) {
                    Platform.runLater(() -> ExceptionWindowController.openExceptionPopup(e.getMessage()));
                }
            }
        });
    }
    
    private void initializeSheetLayoutAndControllers(SheetDTO sheetDTO, RangesDTO rangesDTO) {
        GridBuilder gridBuilder = new GridBuilder(sheetDTO.getLayout().getRow(),
                sheetDTO.getLayout().getColumn(),
                sheetDTO.getLayout().getRowHeight(),
                sheetDTO.getLayout().getColumnWidth());
        
        BorderPane root = (BorderPane) this.mainAppController.getEditorRootElement();
        try {
            root.setCenter(gridBuilder.build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setSheetGridController(gridBuilder.getSheetGridController());
        setCellSubComponentControllerMap(sheetGridController.getCellsControllers());
        sheetGridController.initializeGridModel(sheetDTO.getCells());
        rangesController.initializeRangesModel(rangesDTO);
        fileNotLoadedProperty.set(false);
        actionLineController.resetCellModel();
        rangesController.resetController();
        customizationsController.resetController();
        commandsController.resetController();
        topSubComponentController.setSheetNameAndVersion(
                sheetDTO.getSheetName(), sheetDTO.getVersion());
    }

    public void showCellDetails(CellSubComponentController cellSubComponentController) {
        String selectedCellID = cellSubComponentController.cellIDProperty().get();
        if (this.sheetGridController.isAlreadySelected(selectedCellID)) {
            this.sheetGridController.resetCellModel(selectedCellID);
            this.actionLineController.resetCellModel();
            this.customizationsController.deselectCell();
        } else {
            this.getSingleCellData(selectedCellID);
        }
    }
    
    private void getSingleCellData(String cellID) {
        HttpUrl url = HttpUrl.parse(Constants.GET_SINGLE_CELL_DATA)
                .newBuilder()
                .addQueryParameter("cellID", cellID)
                .build();
        
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        ExceptionWindowController.openExceptionPopup(
                                "Something went wrong: " + e.getMessage())
                );
            }
            
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    String responseBodyString = responseBody.string();
                    if (response.code() != 200) {
                        Platform.runLater(() ->
                                ExceptionWindowController.openExceptionPopup(
                                        "Something went wrong: " + responseBodyString)
                        );
                    } else {
                        CellDTO cellData = Constants.GSON_INSTANCE.fromJson(responseBodyString, CellDTO.class);
                        
                        Platform.runLater(() -> {
                            actionLineController.showCellDetails(cellData);
                            sheetGridController.showSelectedCellAndDependencies(cellData);
                            customizationsController.setSelectedCell(cellData);
                        });
                    }
                }
            }
        });
    }

    public void updateCellValue(String cellToUpdate, String newValue) {
        HttpUrl url = HttpUrl.parse(Constants.UPDATE_CELL_DATA)
                .newBuilder()
                .addQueryParameter("cellID", cellToUpdate)
                .addQueryParameter("newValue", newValue)
                .build();
        
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        ExceptionWindowController.openExceptionPopup(
                                "Something went wrong: " + e.getMessage())
                );
            }
            
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    String responseBodyString = responseBody.string();
                    if (response.code() != 200) {
                        Platform.runLater(() ->
                                ExceptionWindowController.openExceptionPopup(
                                        "Something went wrong: " + responseBodyString)
                        );
                    } else {
                        SheetAndCellDTO sheetAndCellData =
                                Constants.GSON_INSTANCE.fromJson(responseBodyString, SheetAndCellDTO.class);
                        
                        Platform.runLater(() -> {
                            sheetGridController.updateGridModel(sheetAndCellData.getSheetDTO().getCells());
                            actionLineController.showCellDetails(sheetAndCellData.getCellDTO());
                            sheetGridController.showSelectedCellAndDependencies(sheetAndCellData.getCellDTO());
                            topSubComponentController.updateSheetVersion(sheetAndCellData.getSheetDTO().getVersion());
                        });
                    }
                }
            }
        });
    }

    public void getLatestVersionNumber() {
        Request request = new Request.Builder()
                .url(Constants.GET_LATEST_VERSION_NUMBER)
                .build();
        
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        ExceptionWindowController.openExceptionPopup(
                                "Something went wrong: " + e.getMessage())
                );
            }
            
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    String responseBodyString = responseBody.string();
                    if (response.code() != 200) {
                        Platform.runLater(() ->
                                ExceptionWindowController.openExceptionPopup(
                                        "Something went wrong: " + responseBodyString)
                        );
                    } else {
                        Integer versionNumber = Constants.GSON_INSTANCE.fromJson(responseBodyString, Integer.class);
                        
                        Platform.runLater(() -> {
                            topSubComponentController.updateVersionsChoiceBox(versionNumber);
                        });
                    }
                }
            }
        });
    }

    public void loadSheetVersion(int version) {
        HttpUrl url = HttpUrl.parse(Constants.LOAD_SHEET_VERSION)
                .newBuilder()
                .addQueryParameter("version", String.valueOf(version))
                .build();
        
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        ExceptionWindowController.openExceptionPopup(
                                "Something went wrong: " + e.getMessage())
                );
            }
            
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    String responseBodyString = responseBody.string();
                    if (response.code() != 200) {
                        Platform.runLater(() ->
                                ExceptionWindowController.openExceptionPopup(
                                        "Something went wrong: " + responseBodyString)
                        );
                    } else {
                        ColoredSheetDTO coloredSheet =
                                Constants.GSON_INSTANCE.fromJson(responseBodyString, ColoredSheetDTO.class);
                        
                        Platform.runLater(() -> {
                            createReadonlyGrid(coloredSheet, " - version " + version);
                        });
                    }
                }
            }
        });
    }

    public void openGridPopup(GridBuilder gridBuilder, String title, String sheetName) {
        try {
            // Create a new Stage (pop-up window)
            Stage popupStage = new Stage();
            popupStage.setTitle(sheetName + title);

            ScrollPane popupGrid = gridBuilder.build();
            popupGrid.getStyleClass().add("grid-popup");

            // Create a new scene for the pop-up window
            Scene popupScene = new Scene(popupGrid, 600, 400); // Set preferred width and height
            popupStage.setScene(popupScene);
            popupStage.getIcons().add(
                    new Image(Objects.requireNonNull(
                            Main.class.getResourceAsStream(Constants.SHTICELL_LOGO_RESOURCE_LOCATION))));

            // Show the pop-up window
            popupStage.show();
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewRange(String rangeName, String from, String to) {
        HttpUrl url = HttpUrl.parse(Constants.ADD_NEW_RANGE)
                .newBuilder()
                .addQueryParameter("rangeName", rangeName)
                .addQueryParameter("rangeBoundaries", from + ".." + to)
                .build();
        
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        ExceptionWindowController.openExceptionPopup(
                                "Something went wrong: " + e.getMessage())
                );
            }
            
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    String responseBodyString = responseBody.string();
                    if (response.code() != 200) {
                        Platform.runLater(() -> rangesController.updateSaveErrorLabel(responseBodyString));
                    } else {
                        RangeDTO newRange = Constants.GSON_INSTANCE.fromJson(responseBodyString, RangeDTO.class);
                        Platform.runLater(() -> rangesController.acceptNewRange(newRange));
                    }
                }
            }
        });
    }

    public boolean deleteRange(RangeDTO rangeToDelete) {
        try {
            this.engine.removeRange(rangeToDelete.getName());
            return true;
        } catch (RuntimeException e) {
            this.rangesController.updateDeleteErrorLabel(e.getMessage());
            return false;
        }
    }

    public void toggleSelectedRange(RangeDTO selectedRange, RangeDTO previousSelectedRange) {
        this.sheetGridController.toggleSelectedRange(selectedRange, previousSelectedRange);
    }

    public void updateColumnWidth(Integer newColumnWidth, int columnToUpdate) {
        GridPane gridPane = this.getSheetGrid();
        gridPane.getColumnConstraints().get(columnToUpdate).setMinWidth(newColumnWidth);
        gridPane.getColumnConstraints().get(columnToUpdate).setPrefWidth(newColumnWidth);
        gridPane.getColumnConstraints().get(columnToUpdate).setMaxWidth(newColumnWidth);

    }

    public void updateRowHeight(Integer newRowHeight, int rowToUpdate) {
        GridPane gridPane = this.getSheetGrid();
        gridPane.getRowConstraints().get(rowToUpdate).setMinHeight(newRowHeight);
        gridPane.getRowConstraints().get(rowToUpdate).setPrefHeight(newRowHeight);
        gridPane.getRowConstraints().get(rowToUpdate).setMaxHeight(newRowHeight);
    }

    private GridPane getSheetGrid() {
        BorderPane root = (BorderPane) this.mainAppController.getEditorRootElement();
        ScrollPane scrollPane = (ScrollPane) root.getCenter();
        return (GridPane) scrollPane.getContent();
    }


    public void setSelectedColumn(String columnName) {
        int columnIndex = (columnName.charAt(0) - 'A') + 1;
        int currentPrefWidth = (int) this.getSheetGrid().getColumnConstraints().get(columnIndex).getPrefWidth();
        this.customizationsController.setSelectedColumn(columnName, currentPrefWidth);
    }

    public void setSelectedRow(String rowName) {
        int rowIndex = Integer.parseInt(rowName);
        int currentPrefHeight = (int) this.getSheetGrid().getRowConstraints().get(rowIndex).getPrefHeight();
        this.customizationsController.setSelectedRow(rowIndex, currentPrefHeight);
    }

    public void setColumnTextAlignment(String columnName, String alignment) {
        this.cellSubComponentControllerMap.forEach((cellID, cellController) -> {
            if (cellID.contains(columnName)) {
                cellController.setAlignment(alignment);
            }
        });
    }

    public void setCellStyle(String cellID, Color backgroundColor, Color textColor) {
        this.cellSubComponentControllerMap.get(cellID).setCellStyle(backgroundColor, textColor);
        this.engine.updateCellStyle(cellID, backgroundColor, textColor);
    }

    public boolean sortRange(String rangeToSort, List<String> columnsToSortBy) {
        try {
            ColoredSheetDTO sortedSheet = this.engine.sortRangeOfCells(rangeToSort, columnsToSortBy);
            createReadonlyGrid(sortedSheet, " - Sorted");
            return true;
        } catch (ClassCastException e) {
            this.commandsController.updateSortErrorLabel("Cannot sort by non-numeric column");
            return false;
        } catch (RuntimeException e) {
            this.commandsController.updateSortErrorLabel(e.getMessage());
            return false;
        }
    }

    public boolean filterRange(String rangeToFilterBy, String columnToFilterBy, List<Integer> itemsToFilterBy) {
        try {
            ColoredSheetDTO filteredSheet = this.engine.filterRangeOfCells(rangeToFilterBy, columnToFilterBy, itemsToFilterBy);
            createReadonlyGrid(filteredSheet, " - Filtered");
            return true;
        } catch (RuntimeException e) {
            this.commandsController.updateFilterErrorLabel(e.getMessage());
            return false;
        }

    }

    private void createReadonlyGrid(ColoredSheetDTO sheetToShow, String popupName) {
        GridBuilder gridBuilder = new GridBuilder(
                sheetToShow.getLayout().getRow(),
                sheetToShow.getLayout().getColumn(),
                sheetToShow.getLayout().getRowHeight(),
                sheetToShow.getLayout().getColumnWidth());

        this.openGridPopup(gridBuilder, popupName, sheetToShow.getSheetName());
        SheetGridController gridPopupController = gridBuilder.getSheetGridController();
        gridPopupController.initializePopupGridModel(sheetToShow.getCells());

        gridPopupController.getCellsControllers().forEach((cellID, cellController) -> {
            cellController.addOldVersionStyleClass();

            ColoredCellDTO currentCell = sheetToShow.getCells().get(cellID);
            if (currentCell != null) {
                cellController.setCellStyle(currentCell.getBackgroundColor(), currentCell.getTextColor());
            }

        });
    }

    public List<String> getColumnsOfRange(String rangeToFilter) {
        try {
            return this.engine.getColumnsListOfRange(rangeToFilter);
        } catch (RuntimeException e) {
            this.commandsController.updateFilterErrorLabel(e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<EffectiveValueDTO> getUniqueItems(String columnToFilterBy, String rangeToFilter) {
        try {
            return this.engine.getUniqueItemsToFilterBy(columnToFilterBy, rangeToFilter);
        } catch (RuntimeException e) {
            this.commandsController.updateFilterErrorLabel(e.getMessage());
        }

        return Collections.emptyList();
    }

    public static String effectiveValueFormatter(EffectiveValueDTO effectiveValue) {
        String valueToPrint = effectiveValue.getEffectiveValue();
        if (effectiveValue.getType().equals(Constants.BOOLEAN_CELL_TYPE)) {
            valueToPrint = booleanFormatter(valueToPrint);
        } else if (effectiveValue.getType().equals(Constants.NUMERIC_CELL_TYPE)) {
            valueToPrint = numberFormatter(valueToPrint);
        }

        return valueToPrint;
    }

    private static String numberFormatter(String valueToPrint) {
        try {
            double number = Double.parseDouble(valueToPrint);
            DecimalFormat formatter = new DecimalFormat("#,###.##");
            formatter.setRoundingMode(RoundingMode.DOWN);
            return formatter.format(number);
        } catch (Exception ignored) {
            return valueToPrint;
        }
    }

    public static String booleanFormatter(String valueToPrint) {
        return valueToPrint.toUpperCase();
    }

    public boolean buildGraph(String rangeToBuildGraphFrom, String graphType) {
        try {
            LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> graph =
                    this.engine.getGraphFromRange(rangeToBuildGraphFrom);
            this.showGraphPopup(graphType, graph);
            return true;
        } catch (RuntimeException e) {
            this.commandsController.updateGraphErrorLabel(e.getMessage());
            return false;
        }
    }

    private void showGraphPopup(
            String i_GraphType,
            LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> graphData) {
        
        GraphType graphType = GraphType.valueOf(i_GraphType.toUpperCase().replace(" ", "_"));
        Chart graphChart = graphType.createChart(graphData);

        Stage graphStage = new Stage();
        graphStage.setTitle(i_GraphType);
        graphStage.getIcons().add(
                new Image(Objects.requireNonNull(
                        Main.class.getResourceAsStream(Constants.SHTICELL_LOGO_RESOURCE_LOCATION))));
        ScrollPane scrollPane = new ScrollPane();
        (graphChart).setPadding(new Insets(20, 20, 60, 20));
        scrollPane.setContent(graphChart);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        Scene scene = new Scene(scrollPane, 800, 600);
        graphStage.setScene(scene);
        graphStage.showAndWait();  // Show the popup and wait for it to close
    }
    
    public void setAppMainController(MainAppViewController mainAppViewController) {
        this.mainAppController = mainAppViewController;
    }
    
    public void setActive(String sheetName) {
        this.setEngineNameInSession(sheetName);
    }
    
    public void setInActive() {
        // TODO
    }
    
    public void setEngineNameInSession(String sheetName) {
        HttpUrl url = HttpUrl.parse(Constants.SET_ENGINE_TO_SESSION)
                .newBuilder()
                .addQueryParameter("sheetName", sheetName)
                .build();
        
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        ExceptionWindowController.openExceptionPopup(
                                "Something went wrong: " + e.getMessage())
                );
            }
            
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            ExceptionWindowController.openExceptionPopup(
                                    "Something went wrong: " + responseBody)
                    );
                } else {
                    getSheetAndRanges();
                }
            }
        });
    }
}
