package logic.function;

public class Mod implements Math {
    private final String name = "MOD";
    private Math argument1;
    private Math argument2;

    public Mod(Math argument1, Math argument2) {
        this.argument1 = argument1;
        this.argument2 = argument2;
    }

    @Override
    public java.lang.Number invoke() {
        return this.argument1.invoke().doubleValue() % this.argument2.invoke().doubleValue();
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
