package dto.sheet;

import dto.cell.CellDTO;

public class SheetAndCellDTO {
    
    private final SheetDTO sheetDTO;
    private final CellDTO cellDTO;
    
    public SheetAndCellDTO(SheetDTO sheetDTO, CellDTO cellDTO) {
        this.sheetDTO = sheetDTO;
        this.cellDTO = cellDTO;
    }
    
    public SheetDTO getSheetDTO() {return this.sheetDTO;}
    
    public CellDTO getCellDTO() {return this.cellDTO;}
}
