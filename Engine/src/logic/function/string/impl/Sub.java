package logic.function.string.impl;

import logic.function.math.MathFunction;
import logic.function.string.StringFunction;

public class Sub implements StringFunction {
    private final java.lang.String name = "SUB";
    private StringFunction source;
    private MathFunction startindex;
    private MathFunction endIndex;

    public Sub(StringFunction source, MathFunction startindex, MathFunction endIndex) {
        this.source = source;
        this.startindex = startindex;
        this.endIndex = endIndex;
    }

    @Override
    public java.lang.String invoke() {
        int startIndex = this.startindex.invoke().intValue();
        int endIndex = this.endIndex.invoke().intValue();
        int length = this.source.invoke().length();

        return startIndex < 0 || endIndex > length ?
                "!UNDEFINED!" : this.source.invoke().substring(startIndex, endIndex);
    }

    @Override
    public java.lang.String getFunctionName() {
        return this.name;
    }

    @Override
    public java.lang.String toString() {
        return "{" + this.name + "," + this.source + "," + this.startindex.toString() + "," + this.endIndex.toString() + "}";
    }
}
