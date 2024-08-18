package component.impl;

import component.api.Cell;
import logic.function.returnable.Returnable;
import java.util.List;

public class CellImpl implements Cell {
    private final String cellId;
    private int row;
    private int column;
    private String originalValue;
    private Returnable effectiveValue;
    private int version;
    private final List<Cell> dependingOn;
    private final List<Cell> influencingOn;

    public CellImpl(int row, int col, String originalValue, Returnable effectiveValue, int version, List<Cell> dependingOn, List<Cell> influencingOn) {
        this.cellId = Cell.createCellId(row, col);
        this.row = row;
        this.column = col;
        this.originalValue = originalValue;
        this.effectiveValue = effectiveValue;
        this.version = version;
        this.dependingOn = dependingOn;
        this.influencingOn = influencingOn;
    }

    @Override
    public int getRow() {
        return this.row;
    }

    @Override
    public int getColumn() {
        return this.column;
    }

    @Override
    public void setOriginalValue(String Value) {
        this.originalValue = Value;
    }

    @Override
    public String getCellId() {
        return this.cellId;
    }

    @Override
    public String getOriginalValue() {
        return this.originalValue;
    }

    @Override
    public Returnable getEffectiveValue() {
        return this.effectiveValue;
    }

    @Override
    public int version() {
        return this.version;
    }

    @Override
    public List<Cell> getDependentCells() {
        return this.dependingOn;
    }

    @Override
    public List<Cell> getInfluencedCells() {
        return this.influencingOn;
    }
}
