package dto.returnable;

import logic.function.returnable.api.Returnable;

public class EffectiveValueDTO {
    private String effectiveValue;
    private String type;
    
    public EffectiveValueDTO(Returnable returnable) {
        this.effectiveValue = returnable.getValue().toString();
        this.type = returnable.getCellType().toString();
    }
    
    public String getEffectiveValue() {return this.effectiveValue;}
    
    public String getType() {return this.type;}
}
