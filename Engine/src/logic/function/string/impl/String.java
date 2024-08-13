package logic.function.string.impl;

import logic.function.string.StringFunction;

public class String implements StringFunction {
    private java.lang.String value;
    private int length;

    public String(java.lang.String string) {
        this.value = string;
        this.length = string.length();
    }

    @Override
    public java.lang.String invoke() {
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
