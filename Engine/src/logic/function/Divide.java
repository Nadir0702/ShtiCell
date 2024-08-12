package logic.function;

public class Divide  implements Math {
    private final String name = "DIVIDE";
    private Math argument1;
    private Math argument2;

    public Divide(Math argument1, Math argument2) {
        this.argument1 = argument1;
        this.argument2 = argument2;
    }

    @Override
    public java.lang.Number invoke() {
        double denominator = this.argument2.invoke().doubleValue();
        double numerator = this.argument1.invoke().doubleValue();

        return denominator != 0 ? numerator / denominator : Double.NaN;
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "{" + this.name + "," + this.argument1.toString() + "," + this.argument2.toString() + "}";
    }
}
