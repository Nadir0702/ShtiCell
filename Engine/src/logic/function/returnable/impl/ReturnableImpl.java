package logic.function.returnable.impl;

import component.api.CellType;
import logic.function.returnable.api.Returnable;

public class ReturnableImpl implements Returnable {
    private final Object value;
    private final CellType cellType;

    public ReturnableImpl(Object value, CellType type) {
        this.value = value;
        this.cellType = type;
    }

    @Override
    public CellType getCellType() {
        return this.cellType;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public <T> T tryConvertTo(Class<T> type) {
        if (cellType.isAssignableFrom(type)) {
            return type.cast(this.value);
        } else {
            throw new ClassCastException();
        }
    }
}
