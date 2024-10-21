package dto.sheet;

import dto.range.RangesDTO;

public class SheetAndRangesDTO {
    
    private final ColoredSheetDTO coloredSheetDTO;
    private final RangesDTO rangesDTO;
    
    public SheetAndRangesDTO(ColoredSheetDTO coloredSheetDTO, RangesDTO rangesDTO) {
        this.coloredSheetDTO = coloredSheetDTO;
        this.rangesDTO = rangesDTO;
    }
    
    public ColoredSheetDTO getSheetDTO() { return this.coloredSheetDTO; }
    
    public RangesDTO getRangesDTO() { return this.rangesDTO; }
}
