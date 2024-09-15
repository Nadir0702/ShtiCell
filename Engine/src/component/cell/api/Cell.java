package component.cell.api;

import logic.function.returnable.api.Returnable;

import java.io.Serializable;
import java.util.List;

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
    static String createCellID(int row, int col) {
        char column = (char) ('A' + col - 1);
        return "" + column + row;
    }
}
