package dto;

public class SentPermissionRequestDTO {
    
    private String requestedEngineName;
    private String requestedPermission;
    
    public SentPermissionRequestDTO(String requestedEngineName, String requestedPermission) {
        this.requestedEngineName = requestedEngineName;
        this.requestedPermission = requestedPermission;
    }
    
    public String getRequestedEngineName() {
        return this.requestedEngineName;
    }
    
    public String getRequestedPermission() { return this.requestedPermission; }
}
