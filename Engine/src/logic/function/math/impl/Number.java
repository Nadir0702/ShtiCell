package logic.function.math.impl;

import logic.function.math.MathFunction;

public class Number implements MathFunction {
    private java.lang.Number value;

    public Number(java.lang.Number number) {
        this.value = number;
    }

    @Override
    public java.lang.Number invoke() {
        return this.value;
    }

    @Override
    public String getFunctionName() {
        return "";
    }

    @Override
    public String toString() {
        return this.value.doubleValue() < 0 ?
                "(" + this.value + ")" :
                "" + this.value.doubleValue();
    }
}
