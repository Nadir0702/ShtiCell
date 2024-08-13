package logic.function.system;

import logic.function.Function;
import logic.function.UnariFunction;
import logic.function.returnable.Returnable;

public class Ref extends UnariFunction {
    private final String name = "REF";

    public Ref(Function argument) {
        super(argument);
    }

    @Override
    protected Returnable calculate(Returnable argument) {
        return null;
    }

    @Override
    protected boolean validateArgumentTypes(Returnable argument) {
        return false;
    }

    @Override
    public String getFunctionName() {
        return "";
    }
}
