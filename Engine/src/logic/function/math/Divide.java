package logic.function.math;

import logic.function.BinaryFunction;
import logic.function.Function;
import logic.function.returnable.Returnable;
import logic.function.returnable.MyNumber;

public class Divide extends BinaryFunction {
    private final String name = "DIVIDE";

    public Divide(Function argument1, Function argument2) {
        super(argument1, argument2);
    }

    @Override
    protected Returnable calculate(Returnable argument1, Returnable argument2) {
        return validateArgumentsTypes(argument1, argument2) && (double)argument2.getValue() != 0 ?
                new MyNumber((double)argument1.getValue() / (double)argument2.getValue()) :
                new MyNumber(Double.NaN);
    }

    @Override
    protected boolean validateArgumentsTypes(Returnable argument1, Returnable argument2) {
        return argument1 instanceof MyNumber && argument2 instanceof MyNumber;
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }
}
