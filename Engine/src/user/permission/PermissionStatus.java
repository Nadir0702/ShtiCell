package user.permission;

public enum PermissionStatus {
    ACCEPTED("Accepted"),
    DENIED("Denied"),
    PENDING("Pending");
    
    private final String permissionStatus;
    
    PermissionStatus(String permissionStatus) { this.permissionStatus = permissionStatus; }
    
    public String getPermissionStatus() { return this.permissionStatus; }
}
