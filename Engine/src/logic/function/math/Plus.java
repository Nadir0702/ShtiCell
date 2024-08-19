package logic.function.math;

import component.api.CellType;
import logic.function.BinaryFunction;
import logic.function.Function;
import logic.function.returnable.MyNumber;
import logic.function.returnable.Returnable;

public class Plus extends BinaryFunction {
    private final String name = "PLUS";

    public Plus(Function argument1, Function argument2) {
        super(argument1, argument2);
    }

    @Override
    protected Returnable calculate(Returnable argument1, Returnable argument2) {
        /*return validateArgumentsTypes(argument1, argument2) ?
                new Number((double)argument1.getValue() + (double)argument2.getValue()) :
                new Number(Double.NaN);*/

        return new MyNumber(argument1.tryConvertTo(Double.class) + argument2.tryConvertTo(Double.class));
    }

    @Override
    public CellType getReturnType() {
        return CellType.NUMERIC;
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
