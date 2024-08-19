package logic.function.math;

import logic.function.Function;
import logic.function.UnariFunction;
import logic.function.returnable.Returnable;
import logic.function.returnable.MyNumber;

public class Abs extends UnariFunction {
    private final String name = "ABS";

    public Abs(Function argument) {
        super(argument);
    }

    @Override
    protected Returnable calculate(Returnable argument) {
        return validateArgumentTypes(argument) ?
                new MyNumber(Math.abs((double)argument.getValue())) :
                new MyNumber(Double.NaN);
    }

    @Override
    protected boolean validateArgumentTypes(Returnable argument) {
        return argument instanceof MyNumber;
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }
}
