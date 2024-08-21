package logic;

import component.api.Sheet;
import component.impl.SheetImpl;
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
    public void updateSingleCellData(String cellID) {

    }

    @Override
    public void showVersions() {

    }
}
