package component.cell.impl;

import component.cell.api.Cell;
import component.sheet.api.ReadonlySheet;
import component.sheet.api.Sheet;
import component.sheet.impl.SheetImpl;
import logic.function.parser.FunctionParser;
import logic.function.parser.RefParser;
import logic.function.returnable.api.Returnable;
import logic.function.returnable.impl.ReturnableImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static component.sheet.api.Sheet.isValidCellID;

public class CellImpl implements Cell {
    private final ReadonlySheet sheet;
    private final String cellId;
    private int row;
    private int column;
    private String originalValue;
    private Returnable effectiveValue;
    private int version;
    private final List<Cell> dependingOn;
    private final List<Cell> influencingOn;

    public CellImpl(String cellID, String originalValue, int version, ReadonlySheet sheet) {
        this.cellId = cellID;
//        this.row = row;
//        this.column = col;
        this.originalValue = originalValue;
        this.version = version;
        this.dependingOn = new ArrayList<>();
        this.influencingOn = new ArrayList<>();
        this.sheet = sheet;

        RefParser.PARSE.extractRefs(originalValue).stream()
                .filter(Sheet::isValidCellID)
                .forEach(this::setDependantAndInfluencedCells);
    }

    private void setDependantAndInfluencedCells(String dependantCellID) {
        Cell dependantCell = this.sheet.getCell(dependantCellID);

        if (dependantCell == null) {
            dependantCell = new CellImpl(dependantCellID, "", this.version, this.sheet);
            this.sheet.getCells().put(dependantCellID, dependantCell);
        }

        this.dependingOn.add(dependantCell);
        dependantCell.getInfluencedCells().add(this);
    }

    @Override
    public boolean calculateEffectiveValue() {
        Returnable newEffectiveValue = FunctionParser.parseFunction(this.originalValue).invoke(this.sheet);

        if (newEffectiveValue.equals(effectiveValue)) {
            return false;
        } else {
            this.effectiveValue = newEffectiveValue;
            return true;
        }
    }

//    public Sheet updateCell() {
//        SheetImpl newSheetVersion = sheet.copySheet();
//        Cell updatedCell = newSheetVersion.getCell(cellId);
//        if (updatedCell != null) {
//            updatedCell.setOriginalValue(value);
//        } else {
//            updatedCell = new CellImpl(cellId, value, newSheetVersion.getVersion() + 1, newSheetVersion);
//            newSheetVersion.cells.put(cellId, updatedCell);
//        }
//    }

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
    public int getVersion() {
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

    @Override
    public void updateVersion(int newVersion) {
        this.version = newVersion;
    }
}
