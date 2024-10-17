package dto.sheet;

import dto.range.RangesDTO;

public class SheetAndRangesDTO {
    
    private final SheetDTO sheetDTO;
    private final RangesDTO rangesDTO;
    
    public SheetAndRangesDTO(SheetDTO sheetDTO, RangesDTO rangesDTO) {
        this.sheetDTO = sheetDTO;
        this.rangesDTO = rangesDTO;
    }
    
    public SheetDTO getSheetDTO() { return this.sheetDTO; }
    
    public RangesDTO getRangesDTO() { return this.rangesDTO; }
}
