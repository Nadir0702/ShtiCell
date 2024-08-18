package logic.function;

import logic.function.returnable.Returnable;

abstract public class TrinaryFunction implements Function {
    private Function argument1;
    private Function argument2;
    private Function argument3;

    public TrinaryFunction(Function argument1, Function argument2, Function argument3) {
        this.argument1 = argument1;
        this.argument2 = argument2;
        this.argument3 = argument3;
    }

    @Override
    public Returnable invoke() {
        return calculate(this.argument1.invoke(), this.argument2.invoke(), this.argument3.invoke());
    }

    @Override
    public String toString() {
        return "{" + this.getFunctionName() + "," + this.argument1 + "," + this.argument2 + "," + this.argument3 + "}";
    }

    abstract protected Returnable calculate(Returnable argument1, Returnable argument2, Returnable argument3);

    abstract protected boolean validateArgumentsTypes(Returnable argument1, Returnable argument2, Returnable argument3);
}