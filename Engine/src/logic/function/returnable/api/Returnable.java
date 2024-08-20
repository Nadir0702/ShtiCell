package logic.function.returnable.api;

import component.api.CellType;

public interface Returnable {
    CellType getCellType();
    <T> T tryConvertTo(Class<T> type);
}
