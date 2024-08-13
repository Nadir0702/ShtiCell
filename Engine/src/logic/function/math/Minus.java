package logic.function.math;

import logic.function.BinaryFunction;
import logic.function.Function;
import logic.function.returnable.Number;
import logic.function.returnable.Returnable;

public class Minus extends BinaryFunction {
    private final String name = "MINUS";

    public Minus(Function argument1, Function argument2) {
        super(argument1, argument2);
    }

    @Override
    protected Returnable calculate(Returnable argument1, Returnable argument2) {
        return validateArgumentsTypes(argument1, argument2) ?
                new Number((double)argument1.getValue() - (double)argument2.getValue()) :
                new Number(Double.NaN);
    }

    @Override
    protected boolean validateArgumentsTypes(Returnable argument1, Returnable argument2) {
        return argument1 instanceof Number && argument2 instanceof Number;
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }
}
