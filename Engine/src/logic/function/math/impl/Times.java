package logic.function.math.impl;

import logic.function.math.MathFunction;

public class Times  implements MathFunction {
    private final String name = "TIMES";
    private MathFunction argument1;
    private MathFunction argument2;

    public Times(MathFunction argument1, MathFunction argument2) {
        this.argument1 = argument1;
        this.argument2 = argument2;
    }

    @Override
    public java.lang.Number invoke() {
        return this.argument1.invoke().doubleValue() * this.argument2.invoke().doubleValue();
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
