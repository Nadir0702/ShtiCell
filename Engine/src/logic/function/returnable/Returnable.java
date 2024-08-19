package logic.function.returnable;

import logic.function.Function;

public interface Returnable extends Function {
    Object getValue();
    <T> T tryConvertTo(Class<T> type);
}
