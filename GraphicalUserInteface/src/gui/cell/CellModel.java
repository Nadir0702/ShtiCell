package gui.cell;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class CellModel implements ActionLineCellModel, DependenciesCellModel {
    private StringProperty cellIDProperty;
    private StringProperty originalValueProperty;
    private StringProperty lastUpdatedVersionProperty;
    private StringProperty selectedCellProperty;
    private List<String> dependingOnList;
    private List<String> influencingOnList;
    
    public CellModel() {
        this.cellIDProperty = new SimpleStringProperty("");
        this.originalValueProperty = new SimpleStringProperty();
        this.lastUpdatedVersionProperty = new SimpleStringProperty("");
        this.selectedCellProperty = new SimpleStringProperty();
        this.dependingOnList = new ArrayList<>();
        this.influencingOnList = new ArrayList<>();
    }
    
    
    @Override
    public void bind(StringProperty cellIDProperty,
                     StringProperty originalValueProperty,
                     StringProperty lastUpdatedVersionProperty) {
        
        cellIDProperty.bind(Bindings.concat("Cell ID ", this.cellIDProperty));
        
        lastUpdatedVersionProperty.bind(
                Bindings.concat("Last Updated Version ", this.lastUpdatedVersionProperty));
        
        this.bindOriginalValue(originalValueProperty);
    }
    
    @Override
    public StringProperty getCellIDProperty() {
        return this.cellIDProperty;
    }
    
    @Override
    public StringProperty getOriginalValueProperty() {
        return this.originalValueProperty;
    }
    
    @Override
    public StringProperty getLastUpdatedVersionProperty() {
        return this.lastUpdatedVersionProperty;
    }
    
    @Override
    public void bindOriginalValue(StringProperty originalValueProperty) {
        originalValueProperty.bind(this.originalValueProperty);
    }
    
    @Override
    public StringProperty getSelectedCellProperty() {
        return this.selectedCellProperty;
    }
    
    @Override
    public List<String> getDependingOn() {
        return this.dependingOnList;
    }
    
    @Override
    public List<String> getInfluencingOn() {
        return this.influencingOnList;
    }
    
    @Override
    public void setDependingOn(List<String> dependingOn) {
        this.dependingOnList.clear();
        this.dependingOnList.addAll(dependingOn);
    }
    
    @Override
    public void setInfluencingOn(List<String> influencingOn) {
        this.influencingOnList.clear();
        this.influencingOnList.addAll(influencingOn);
    }
    
    @Override
    public void setSelectedCell(String cellId) {
        this.selectedCellProperty.set(cellId);
    }
}
