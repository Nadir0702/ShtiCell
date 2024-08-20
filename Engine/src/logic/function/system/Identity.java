package logic.function.system;

import component.api.CellType;
import logic.function.Function;
import logic.function.returnable.api.Returnable;
import logic.function.returnable.impl.ReturnableImpl;

public class Identity implements Function {
    private final Object value;
    private final CellType type;

    public Identity(Object value, CellType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public String getFunctionName() {
        return "IDENTITY";
    }

    @Override
    public Returnable invoke() {
        return new ReturnableImpl(this.value, this.type);
    }

    @Override
    public CellType getReturnType() {
        return this.type;
    }
}
