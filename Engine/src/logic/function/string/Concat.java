package logic.function.string;

import logic.function.BinaryFunction;
import logic.function.Function;
import logic.function.returnable.Returnable;
import logic.function.returnable.MyString;

public class Concat extends BinaryFunction {
    private final String name = "CONCAT";

    public Concat(Function argument1, Function argument2) {
        super(argument1, argument2);
    }

    @Override
    public Returnable calculate(Returnable argument1, Returnable argument2) {
        return validateArgumentsTypes(argument1, argument2) ?
                new MyString((String)argument1.getValue() + argument2.getValue()) :
                new MyString("!UNDEFINED!");
    }

    @Override
    protected boolean validateArgumentsTypes(Returnable argument1, Returnable argument2) {
        return argument1 instanceof MyString && argument2 instanceof MyString;
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }
}

