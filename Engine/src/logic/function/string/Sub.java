package logic.function.string;

import component.api.CellType;
import logic.function.Function;
import logic.function.TrinaryFunction;
import logic.function.returnable.api.Returnable;
import logic.function.returnable.impl.ErrorValue;
import logic.function.returnable.impl.ReturnableImpl;

public class Sub extends TrinaryFunction {
    private final String name = "CONCAT";

    public Sub(Function argument1, Function argument2, Function argument3) {
        super(argument1, argument2, argument3);
    }

    @Override
    public Returnable calculate(Returnable source, Returnable startIndex, Returnable endIndex) {
        try {
            return new ReturnableImpl(
                    source.tryConvertTo(String.class).substring(startIndex.tryConvertTo(Integer.class),
                                                                endIndex.tryConvertTo(Integer.class)),
                    CellType.STRING);
        } catch (ClassCastException e) {
            return ErrorValue.UNDEFINED;
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

