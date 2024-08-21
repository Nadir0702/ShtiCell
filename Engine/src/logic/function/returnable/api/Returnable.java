package logic.function.returnable.api;

import component.api.CellType;

public interface Returnable {
    CellType getCellType();
    Object getValue();
    <T> T tryConvertTo(Class<T> type);
}
