package logic.function.returnable;

public class String implements Returnable {
    private java.lang.String value;

    public String(java.lang.String string) {
        this.value = string;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public <T> T tryConvertTo(Class<T> type) {
        if (String.class.isAssignableFrom(type)) {
            return type.cast(this.value);
        }

        return null;
    }

    @Override
    public java.lang.String toString() {
        return this.value;
    }
}
