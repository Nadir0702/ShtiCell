package logic.function.returnable;

public class Boolean implements Returnable {
    private boolean value;

    public Boolean(boolean value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public <T> T tryConvertTo(Class<T> type) {
        if(Boolean.class.isAssignableFrom(type)) {
            return type.cast(this.value);
        }

        return null;
    }

    @Override
    public java.lang.String toString() {
        return this.value ? "TRUE" : "FALSE";
    }
}
