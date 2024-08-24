package logic;

import component.cell.api.Cell;
import component.cell.impl.CellImpl;
import component.sheet.api.Sheet;
import component.sheet.impl.SheetImpl;
import dto.CellDTO;
import dto.SheetDTO;

public class EngineImpl implements Engine{
    private Sheet sheet = null;

    public EngineImpl() {
        this.sheet = new SheetImpl("testing");

    }

    @Override
    public boolean LoadData(String path) {
        return false;
    }

    @Override
    public SheetDTO getSheetAsDTO() {
        return new SheetDTO(this.sheet);
    }

    @Override
    public CellDTO getSingleCellData(String cellID) {
        return new CellDTO(this.sheet.getCell(cellID));
    }

    @Override
    public void updateSingleCellData(String cellID, String value) {
        SheetImpl newSheetVersion = this.sheet.copySheet();
        updateCell(cellID, value, newSheetVersion);
        this.sheet = this.sheet.updateSheet(cellID, value, newSheetVersion);
    }

    private void updateCell(String cellID, String value, Sheet newSheetVersion) {
        Cell cellToUpdate = newSheetVersion.getCell(cellID);
        if (cellToUpdate != null) {
            cellToUpdate.setOriginalValue(value);
        } else {
            cellToUpdate = new CellImpl(cellID, value, newSheetVersion.getVersion() + 1, newSheetVersion);
            newSheetVersion.getCells().put(cellID, cellToUpdate);
        }
    }

    @Override
    public void showVersions() {

    }
}
