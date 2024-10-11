package user;

import dto.SentPermissionRequestDTO;
import user.permission.PermissionStatus;
import user.permission.PermissionType;
import user.request.api.PermissionRequestInOwner;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String userName;
    private List<PermissionRequestInOwner> permissionRequests;
    
    public User(String userName) {
        this.userName = userName;
        this.permissionRequests = new ArrayList<>();
    }
    
    public String getUserName() { return this.userName; }
    
    public void createPermissionRequest(String requestedPermission, String engineName, String sender) {
        this.permissionRequests.add(PermissionRequestInOwner.create(
                PermissionType.valueOf(requestedPermission), PermissionStatus.PENDING, engineName, sender
        ));
    }
}
