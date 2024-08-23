package component.cell.api;

import logic.function.returnable.api.Returnable;

import java.io.Serializable;
import java.util.List;

public interface Cell extends Serializable {
    String getCellId();
    int getRow();
    int getColumn();
    String getOriginalValue();
    void setOriginalValue(String Value);
    Returnable getEffectiveValue();
    boolean calculateEffectiveValue();
    int getVersion();
    List<Cell> getDependentCells();
    List<Cell> getInfluencedCells();
    void updateVersion(int newVersion);

    //static String createCellId(int row, int column) { return row + ":" + column; }
}
