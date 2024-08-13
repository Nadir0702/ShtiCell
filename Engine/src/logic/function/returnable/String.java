package logic.function.returnable;

public class String implements Returnable {
    private java.lang.String value;

    public String(java.lang.String string) {
        this.value = string;
    }

    @Override
    public Returnable invoke() {
        return this;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public java.lang.String getFunctionName() {
        return "";
    }

    @Override
    public java.lang.String toString() {
        return this.value;
    }
}
