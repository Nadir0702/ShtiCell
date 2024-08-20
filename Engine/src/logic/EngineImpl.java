package logic;

import component.api.Sheet;
import dto.SheetDTO;

public class EngineImpl implements Engine{
    private Sheet sheet = null;

    @Override
    public boolean LoadData(String path) {
        return false;
    }

    @Override
    public SheetDTO getSheetAsDTO() {
        return new SheetDTO(this.sheet);
    }

    @Override
    public void getSingleCellData(String cellID) {

    }

    @Override
    public void updateSingleCellData(String cellID) {

    }

    @Override
    public void showVersions() {

    }
}
