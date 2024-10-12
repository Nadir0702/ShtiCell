package dto;

public class ReceivedPermissionRequestDTO {
    private final String sender;
    private final String requestedEngineName;
    private final String requestedPermission;
    
    public ReceivedPermissionRequestDTO(String sender, String requestedEngineName, String requestedPermission) {
        this.sender = sender;
        this.requestedEngineName = requestedEngineName;
        this.requestedPermission = requestedPermission;
    }
    
    public String getSender() { return this.sender;}
    
    public String getRequestedEngineName() {
        return this.requestedEngineName;
    }
    
    public String getRequestedPermission() { return this.requestedPermission; }
}
