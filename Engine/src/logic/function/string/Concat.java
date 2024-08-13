package logic.function.string;

import logic.function.BinaryFunction;
import logic.function.Function;
import logic.function.returnable.Returnable;
import logic.function.returnable.String;

public class Concat extends BinaryFunction {
    private final java.lang.String name = "CONCAT";

    public Concat(Function argument1, Function argument2) {
        super(argument1, argument2);
    }

    @Override
    public Returnable calculate(Returnable argument1, Returnable argument2) {
        return validateArgumentsTypes(argument1, argument2) ?
                new String((java.lang.String)argument1.getValue() + argument2.getValue()) :
                new String("!UNDEFINED!");
    }

    @Override
    protected boolean validateArgumentsTypes(Returnable argument1, Returnable argument2) {
        return argument1 instanceof String && argument2 instanceof String;
    }

    @Override
    public java.lang.String getFunctionName() {
        return this.name;
    }
}

