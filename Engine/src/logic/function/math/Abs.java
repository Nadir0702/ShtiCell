package logic.function.math;

import logic.function.Function;
import logic.function.UnariFunction;
import logic.function.returnable.Returnable;
import logic.function.returnable.Number;

public class Abs extends UnariFunction {
    private final String name = "ABS";

    public Abs(Function argument) {
        super(argument);
    }

    @Override
    protected Returnable calculate(Returnable argument) {
        return validateArgumentTypes(argument) ?
                new Number(Math.abs((double)argument.getValue())) :
                new Number(Double.NaN);
    }

    @Override
    protected boolean validateArgumentTypes(Returnable argument) {
        return argument instanceof Number;
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }
}
