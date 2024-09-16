package component.cell.api;

import logic.function.returnable.api.Returnable;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface Cell extends Serializable {
    String getCellId();
    String getOriginalValue();
    void setOriginalValue(String Value, int newVersion);
    Returnable getEffectiveValue();
    boolean calculateEffectiveValue();
    int getVersion();
    List<Cell> getDependentCells();
    List<Cell> getInfluencedCells();
    void updateVersion(int newVersion);
    Set<String> getUsedRanges();
    
    static String createCellID(int row, int col) {
        char column = (char) ('A' + col - 1);
        return "" + column + row;
    }
}
