package component.cell.impl;

import component.cell.api.Cell;
import component.range.api.Range;
import component.sheet.api.ReadonlySheet;
import component.sheet.api.Sheet;
import logic.function.parser.FunctionParser;
import logic.function.parser.OriginalValueParser;
import logic.function.returnable.api.Returnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CellImpl implements Cell {
    private final ReadonlySheet sheet;
    private final String cellId;
    private String originalValue;
    private Returnable effectiveValue;
    private int version;
    private final List<Cell> dependingOn;
    private final List<Cell> influencingOn;

    public CellImpl(String cellID, String originalValue, int version, ReadonlySheet sheet) {
        this.cellId = Character.toUpperCase(cellID.charAt(0)) + cellID.substring(1);
        this.originalValue = originalValue;
        this.version = version;
        this.dependingOn = new ArrayList<>();
        this.influencingOn = new ArrayList<>();
        this.sheet = sheet;

        this.getInfluencingCellsFromDummy();
        this.setDependencies();
    }

    private void getInfluencingCellsFromDummy(){
        Cell dummyCell = this.sheet.getCell(this.cellId);
        if(dummyCell != null){
            this.influencingOn.addAll(dummyCell.getInfluencedCells());
        }
    }

    private void setDependantAndInfluencedCells(String dependantCellID) {
        Cell dependantCell = this.sheet.getCell(dependantCellID);

        if (dependantCell == null) {
            dependantCell = new CellImpl(dependantCellID, "", this.version, this.sheet);
            this.sheet.getCells().put(dependantCell.getCellId(), dependantCell);
        }

        this.dependingOn.add(dependantCell);
        dependantCell.getInfluencedCells().add(this);
    }

    private void setDependencies(){
        Set<String> dependencies = new HashSet<>(this.getRefDependencies());
        dependencies.addAll(this.getRangesDependencies());
        
        dependencies.forEach(this::setDependantAndInfluencedCells);
    }
    
    private Set<String> getRangesDependencies() {
        Set<String> dependencies = new HashSet<>();
        
        this.getUsedRanges()
                .stream()
                .filter(this.sheet::isExistingRange)
                .forEach((rangeName) -> {
                    Range currentRange = this.sheet.getRanges().get(rangeName);
                    currentRange.addUsage();
                    currentRange.getRangeCells().forEach(cell -> dependencies.add(cell.getCellId()));
                });
        
        return dependencies;
    }
    
    private Set<String> getRefDependencies() {
        return OriginalValueParser.REF.extract(this.originalValue).stream()
                .filter(Sheet::isValidCellID)
                .collect(Collectors.toSet());
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

    @Override
    public void setOriginalValue(String value, int newVersion) {
        this.originalValue = value;
        
        this.dependingOn.forEach(cell -> cell.getInfluencedCells().remove(this));
//        for(Cell cell : this.dependingOn){
//            cell.getInfluencedCells().remove(this);
//        }
        
        this.dependingOn.clear();
        this.setDependencies();
        
//        if (value.equals(this.originalValue)) {
//        }
        this.version = newVersion;
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
    
    @Override
    public Set<String> getUsedRanges() {
        Set<String> usedRanges = OriginalValueParser.SUM.extract(this.originalValue);
        usedRanges.addAll(OriginalValueParser.AVERAGE.extract(this.originalValue));
        return usedRanges;
    }
}
