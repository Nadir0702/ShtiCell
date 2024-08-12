package logic.function;

public class Abs implements Math {
    private final String name = "ABS";
    private Math argument;

    public Abs(Math argument) {
        this.argument = argument;
    }

    @Override
    public java.lang.Number invoke() {
        return java.lang.Math.abs(this.argument.invoke().doubleValue());
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "{" + this.name + "," + this.argument.toString() + "}";
    }
}
