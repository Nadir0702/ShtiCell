package logic.function.returnable;

import component.api.CellType;

public class MyBoolean implements Returnable {
    private boolean value;

    public MyBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public <T> T tryConvertTo(Class<T> type) {
        if(MyBoolean.class.isAssignableFrom(type)) {
            return type.cast(this.value);
        }

        return null;
    }

    @Override
    public String getFunctionName() {
        return MyBoolean.class.getSimpleName();
    }

    @Override
    public Returnable invoke() {
        return this;
    }

    @Override
    public CellType getReturnType() {
        return CellType.BOOLEAN;
    }

    @Override
    public String toString() {
        return this.value ? "TRUE" : "FALSE";
    }
}
