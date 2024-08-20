package dto;

import component.api.Cell;
import logic.function.returnable.api.Returnable;

import java.util.ArrayList;
import java.util.List;

public class CellDTO {
    private final String cellId;
    private final String originalValue;
    private final Returnable effectiveValue;
    private final int version;
    private final List<String> dependingOn;
    private final List<String> influencingOn;

    public CellDTO(Cell cell) {
        this.cellId = cell.getCellId();
        this.originalValue = cell.getOriginalValue();
        this.effectiveValue = cell.getEffectiveValue();
        this.version = cell.getVersion();
        this.dependingOn = new ArrayList<>();
        this.influencingOn = new ArrayList<>();

        for (Cell dependantCell : cell.getDependentCells()){
            this.dependingOn.add(dependantCell.getCellId());
        }

        for (Cell influencedCell : cell.getInfluencedCells()){
            this.dependingOn.add(influencedCell.getCellId());
        }

    }

    public String getCellId() {
        return cellId;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public Returnable getEffectiveValue() {
        return effectiveValue;
    }

    public int getVersion() {
        return version;
    }

    public List<String> getDependingOn() {
        return dependingOn;
    }

    public List<String> getInfluencingOn() {
        return influencingOn;
    }
}
