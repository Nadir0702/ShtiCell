package component.sheet.api;

import java.io.Serializable;

public interface UpdatableSheet extends Serializable {
    Sheet updateCellValueAndCalculate(String cellId, String value);
}
