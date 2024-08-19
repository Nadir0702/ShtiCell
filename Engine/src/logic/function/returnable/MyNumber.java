package logic.function.returnable;

import component.api.CellType;

public class MyNumber implements Returnable {
    private double value;

    public MyNumber(double number) {
        this.value = number;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public <T> T tryConvertTo(Class<T> type) {
        if(Double.class.isAssignableFrom(type)) {
            return type.cast(this.value);
        }
        else{
            throw new ClassCastException("Cannot convert double to " + type);
        }
    }

    @Override
    public String getFunctionName() {
        return MyNumber.class.getSimpleName();
    }

    @Override
    public Returnable invoke() {
        return this;
    }

    @Override
    public CellType getReturnType() {
        return null;
    }

    @Override
    public String toString() {
        return this.value < 0 ?
                "(" + this.value + ")" :
                "" + this.value;
    }
}
