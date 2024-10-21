package dto.sheet;

import dto.cell.CellDTO;
import dto.cell.ColoredCellDTO;

public class SheetAndCellDTO {
    
    private final ColoredSheetDTO coloredSheetDTO;
    private final CellDTO cellDTO;
    
    public SheetAndCellDTO(ColoredSheetDTO coloredSheetDTO, CellDTO cellDTO) {
        this.coloredSheetDTO = coloredSheetDTO;
        this.cellDTO = cellDTO;
    }
    
    public ColoredSheetDTO getSheetDTO() { return this.coloredSheetDTO; }
    
    public CellDTO getCellDTO() { return this.cellDTO; }
}
