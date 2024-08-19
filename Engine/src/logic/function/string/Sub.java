package logic.function.string;

import logic.function.Function;
import logic.function.TrinaryFunction;
import logic.function.returnable.Returnable;
import logic.function.returnable.MyString;
import logic.function.returnable.MyNumber;

public class Sub extends TrinaryFunction {
    private final String name = "CONCAT";

    public Sub(Function argument1, Function argument2, Function argument3) {
        super(argument1, argument2, argument3);
    }

    @Override
    public Returnable calculate(Returnable source, Returnable startIndex, Returnable endIndex) {
        MyString subString = new MyString("!UNDEFINED!");

        if (this.validateArgumentsTypes(source, startIndex, endIndex)){
            if ((double)startIndex.getValue() >= 0 &&
                    (double)endIndex.getValue() <= ((String)source.getValue()).length()){
                subString = new MyString(((String)source.getValue()).substring(
                                (int)endIndex.getValue(), (int)startIndex.getValue()));
            }
        }

        return subString;
    }

    @Override
    protected boolean validateArgumentsTypes(Returnable argument1, Returnable argument2, Returnable argument3) {
        return argument1 instanceof MyString && argument2 instanceof MyNumber && argument3 instanceof MyNumber;
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }
}

