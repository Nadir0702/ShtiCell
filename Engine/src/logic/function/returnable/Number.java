package logic.function.returnable;

public class Number implements Returnable {
    private double value;

    public Number(double number) {
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
    public java.lang.String toString() {
        return this.value < 0 ?
                "(" + this.value + ")" :
                "" + this.value;
    }
}
