package logic.function.returnable;

import component.api.CellType;

public class MyString implements Returnable {
    private String value;

    public MyString(String string) {
        this.value = string;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public <T> T tryConvertTo(Class<T> type) {
        if (MyString.class.isAssignableFrom(type)) {
            return type.cast(this.value);
        }

        return null;
    }

    @Override
    public String getFunctionName() {
        return MyString.class.getSimpleName();
    }

    @Override
    public Returnable invoke() {
        return this;
    }

    @Override
    public CellType getReturnType() {
        return CellType.STRING;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
