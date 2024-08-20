package logic.function.math;

import component.api.CellType;
import logic.function.Function;
import logic.function.UnariFunction;
import logic.function.returnable.impl.ErrorValue;
import logic.function.returnable.api.Returnable;
import logic.function.returnable.impl.ReturnableImpl;

public class Abs extends UnariFunction {
    private final String name = "ABS";

    public Abs(Function argument) {
        super(argument);
    }

    @Override
    protected Returnable calculate(Returnable argument) {
        try {
            return new ReturnableImpl(Math.abs(argument.tryConvertTo(Double.class)), argument.getCellType());
        } catch (ClassCastException e) {
            return ErrorValue.NAN;
        }
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }

    @Override
    public CellType getReturnType() {
        return CellType.NUMERIC;
    }
}
