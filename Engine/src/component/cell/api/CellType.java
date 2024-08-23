package component.cell.api;

import logic.function.returnable.impl.ErrorValue;

public enum CellType {
    NUMERIC(Double.class),
    STRING(String.class),
    BOOLEAN(Boolean.class),
    UNKNOWN(Void.class),
    NO_VALUE(ErrorValue.class);

    private Class<?> type;

    CellType(Class<?> type) {
        this.type = type;
    }

    public boolean isAssignableFrom(Class<?> aType) {
        return type.isAssignableFrom(aType);
    }
}