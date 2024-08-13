package logic.function.returnable;

public class Number implements Returnable {
    private double value;

    public Number(double number) {
        this.value = number;
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
        return this.value < 0 ?
                "(" + this.value + ")" :
                "" + this.value;
    }
}
