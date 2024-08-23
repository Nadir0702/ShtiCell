package logic.function.returnable.impl;

import component.cell.api.CellType;
import logic.function.returnable.api.Returnable;

import java.util.Objects;

public class ReturnableImpl implements Returnable {
    private final Object value;
    private final CellType cellType;

    public ReturnableImpl(Object value, CellType type) {
        this.value = value;
        this.cellType = type;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public CellType getCellType() {
        return this.cellType;
    }

    @Override
    public <T> T tryConvertTo(Class<T> type) {
        if (cellType.isAssignableFrom(type)) {
            return type.cast(this.value);
        } else {
            throw new ClassCastException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReturnableImpl that = (ReturnableImpl) o;

        if (cellType != that.cellType) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = cellType != null ? cellType.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
