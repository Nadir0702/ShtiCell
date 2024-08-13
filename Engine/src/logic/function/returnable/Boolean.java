package logic.function.returnable;

public class Boolean implements Returnable {
    private boolean value;

    public Boolean(boolean value) {
        this.value = value;
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
        return this.value ? "TRUE" : "FALSE";
    }
}
