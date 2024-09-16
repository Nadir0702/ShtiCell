package component.range.impl;

import component.cell.api.Cell;
import component.cell.impl.CellImpl;
import component.range.api.Range;
import component.sheet.api.ReadonlySheet;
import component.sheet.api.Sheet;

import java.util.ArrayList;
import java.util.List;

public class RangeImpl implements Range {
    private String name;
    private List<Cell> cells;
    private int numOfUsages;
    
    
    public RangeImpl(String name, String range, ReadonlySheet sheet) {
        this.name = name;
        this.numOfUsages = 0;
        String[] edges = range.split("\\.\\.");
        
        if(edges.length != 2) {
            throw new IllegalArgumentException("Range must have exactly two edges");
        } else if (!Sheet.isValidCellID(edges[0]) || !Sheet.isValidCellID(edges[1])) {
            throw new IllegalArgumentException("Expected valid cell ID but got " + edges[0] + " and " + edges[1]);
        } else if (!sheet.cellInLayout(edges[0]) || !sheet.cellInLayout(edges[1])) {
            throw new IllegalArgumentException("Range exceeds sheet boundaries");
        }
        
        this.cells = this.createRange(edges[0], edges[1], sheet);
        
    }
    
    private List<Cell> createRange(String from, String to, ReadonlySheet sheet) {
        List<Cell> cells = new ArrayList<>();
        int fromRow = Integer.parseInt(from.substring(1));
        int toRow = Integer.parseInt(to.substring(1));
        int fromCol = Integer.parseInt(from.substring(0, 1));
        int toCol = Integer.parseInt(to.substring(0, 1));
        
        
        for (int row = fromRow; row <= toRow; row++) {
            for (int col = fromCol; col <= toCol; col++) {
                String currentCellId = Cell.createCellID(row, col);
                Cell currentCell = sheet.getCells().get(currentCellId);
                
                if (currentCell == null) {
                    currentCell = new CellImpl(currentCellId, "", sheet.getVersion(), sheet);
                    sheet.getCells().put(currentCell.getCellId(), currentCell);
                }
                
                cells.add(currentCell);
                
            }
        }
        
        return cells;
    }
    
    @Override
    public List<Cell> getRangeCells() {
        return this.cells;
    }
    
    @Override
    public Cell getFrom() {
        return this.cells.getFirst();
    }
    
    @Override
    public Cell getTo() {
        return this.cells.getLast();
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void addUsage() {
        this.numOfUsages++;
    }
    
    @Override
    public void reduceUsage() {
        this.numOfUsages--;
    }
    
    @Override
    public boolean isInUse() {
        return this.numOfUsages > 0;
    }
}
