package logic.function.string.impl;

import logic.function.string.StringFunction;

public class Concat implements StringFunction {
    private final java.lang.String name = "CONCAT";
    private StringFunction argument1;
    private StringFunction argument2;

    public Concat(StringFunction argument1, StringFunction argument2) {
        this.argument1 = argument1;
        this.argument2 = argument2;
    }

    @Override
    public java.lang.String invoke() {
        return this.argument1.invoke() + this.argument2.invoke();
    }

    @Override
    public java.lang.String getFunctionName() {
        return this.name;
    }

    @Override
    public java.lang.String toString() {
        return "{" + this.name + "," + this.argument1 + "," + this.argument2 + "}";
    }
}
