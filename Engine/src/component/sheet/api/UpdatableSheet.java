package component.sheet.api;

import component.sheet.impl.SheetImpl;

import java.io.Serializable;

public interface UpdatableSheet extends Serializable {
    Sheet updateSheet(String cellId, String value, SheetImpl newSheetVersion);
}
