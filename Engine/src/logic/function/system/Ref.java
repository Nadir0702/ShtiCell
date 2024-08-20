package logic.function.system;

import component.api.CellType;
import logic.function.Function;
import logic.function.UnariFunction;
import logic.function.returnable.api.Returnable;

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
    public String getFunctionName() {
        return this.name;
    }

    @Override
    public CellType getReturnType() {
        this.getArgument().getReturnType();
        return null;
    }
}
