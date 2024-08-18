package component.api;

import logic.function.returnable.Returnable;

import java.util.List;

public interface Cell {
    String getCellId();
    int getRow();
    int getColumn();
    String getOriginalValue();
    void setOriginalValue(String Value);
    Returnable getEffectiveValue();
    int version();
    List<Cell> getDependentCells();
    List<Cell> getInfluencedCells();

    static String createCellId(int row, int column) {
        return row + ":" + column;
    }
}
