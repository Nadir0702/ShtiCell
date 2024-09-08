package gui.grid;

import gui.cell.CellSubComponent;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GridBuilder {
    
    private final List<CellSubComponent> cells;
    private final int numOfRows;
    private final int numOfCols;
    private final int rowHeight;
    private final int columnWidth;
    
    public GridBuilder(int row, int col, int rowHeight, int colWidth) {
        this.numOfRows = row;
        this.numOfCols = col;
        this.rowHeight = rowHeight;
        this.columnWidth = colWidth;
        this.cells = new ArrayList<>(numOfRows * numOfCols);
    }
    
    public ScrollPane build() throws IOException {
        
        ScrollPane root = new ScrollPane();
        GridPane grid = new GridPane();
        
        this.buildScrollPane(root);
        this.buildGridPane(grid);
        this.buildRowConstraints(grid);
        this.buildColumnConstraints(grid);
        
        ObservableList<Node> children = grid.getChildren();
        
        this.buildHeadersRow(children);
        this.buildHeadersColumn(children);
        this.buildCellsComponents(children);
        
        root.setContent(grid);
        
        return root;
    }
    
    private void buildCellsComponents(ObservableList<Node> children) throws IOException {
        for (int i = 1; i <= this.numOfRows; i++) {
            for (int j = 1; j <= this.numOfCols; j++) {
                FXMLLoader loader = new FXMLLoader();
                URL cellFXML = getClass().getResource("/gui/cell/CellSubComponent.fxml");
                loader.setLocation(cellFXML);
                Button cell = loader.load();
                for(CellSubComponent currentCell: cells ) {
                    currentCell = loader.getController();
                }
                GridPane.setColumnIndex(cell, j);
                GridPane.setRowIndex(cell, i);
                children.add(cell);
            }
        }
    }
    
    private void buildHeadersColumn(ObservableList<Node> children) {
        for (int i = 1; i <= this.numOfRows; i++) {
            Button button = new Button(i < 10 ? "0" + i : i + "");
            button.setMaxHeight(Double.MAX_VALUE);
            button.setMaxWidth(Double.MAX_VALUE);
            GridPane.setRowIndex(button, i);
            children.add(button);
        }
    }
    
    private void buildHeadersRow(ObservableList<Node> children) {
        for (int i = 0; i < this.numOfCols; i++) {
            Button button = new Button(Character.toString((char) i + 'A'));
            button.setMaxHeight(Double.MAX_VALUE);
            button.setMaxWidth(Double.MAX_VALUE);
            GridPane.setColumnIndex(button, i + 1);
            children.add(button);
        }
    }
    
    private void buildGridPane(GridPane grid) {
        grid.setAlignment(Pos.CENTER); // Set alignment to center
        grid.setMaxHeight(Double.MAX_VALUE); // Set to use maximum available height
        grid.setMaxWidth(Double.MAX_VALUE);  // Set to use maximum available width
        grid.getStyleClass().add("sheet-grid-pane");
    }
    
    private void buildScrollPane(ScrollPane scrollPane) {
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxHeight(Double.MAX_VALUE);
        scrollPane.setMaxWidth(Double.MAX_VALUE);
        scrollPane.setMinHeight(0);
        scrollPane.setMinWidth(0);
        scrollPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/gui/resources/MainGridComponent.css")).toExternalForm());
    }
    
    private void buildRowConstraints(GridPane grid) {
        ObservableList<RowConstraints> rowConstraints = grid.getRowConstraints();
        
        for (int i = 0; i <= this.numOfRows; i++) {
            RowConstraints currentRow = new RowConstraints();
            
            currentRow.setVgrow(Priority.ALWAYS);
            if (i == 0) {
                currentRow.setMaxHeight(35);
                currentRow.setMinHeight(35);
                currentRow.setPrefHeight(35);
            } else {
                currentRow.setMaxHeight(Double.MAX_VALUE);
                currentRow.setMinHeight(this.rowHeight);
                currentRow.setPrefHeight(this.rowHeight);
            }
            
            rowConstraints.add(currentRow);
        }
    }
    
    private void buildColumnConstraints(GridPane grid) {
        ObservableList<ColumnConstraints> columnConstraints = grid.getColumnConstraints();
        
        for (int i = 0; i <= this.numOfCols; i++) {
            ColumnConstraints currentColumn = new ColumnConstraints();
            
            currentColumn.setHgrow(Priority.ALWAYS);
            if (i == 0) {
                currentColumn.setMaxWidth(35);
                currentColumn.setMinWidth(35);
                currentColumn.setPrefWidth(35);
            } else {
                currentColumn.setMaxWidth(Double.MAX_VALUE);
                currentColumn.setMinWidth(this.columnWidth);
                currentColumn.setPrefWidth(this.columnWidth);
            }
            
            columnConstraints.add(currentColumn);
        }
    }
}
