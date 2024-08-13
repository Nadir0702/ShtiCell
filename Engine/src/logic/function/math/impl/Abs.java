package logic.function.math.impl;

import logic.function.math.MathFunction;

public class Abs implements MathFunction {
    private final String name = "ABS";
    private MathFunction argument;

    public Abs(MathFunction argument) {
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
