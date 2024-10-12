package user;

import dto.ReceivedPermissionRequestDTO;

import user.permission.PermissionStatus;
import user.permission.PermissionType;
import user.request.api.PermissionRequestInOwner;

import java.util.LinkedHashSet;
import java.util.Set;

public class User {
    private final String userName;
    private Set<PermissionRequestInOwner> permissionRequests;
    
    public User(String userName) {
        this.userName = userName;
        this.permissionRequests = new LinkedHashSet<>();
    }
    
    public String getUserName() { return this.userName; }
    
    public Set<ReceivedPermissionRequestDTO> getPermissionRequests() {
        Set<ReceivedPermissionRequestDTO> receivedPermissionRequests = new LinkedHashSet<>();
        permissionRequests.forEach(permissionRequest ->
            receivedPermissionRequests.add(new ReceivedPermissionRequestDTO(
                    permissionRequest.getRequestSenderUserName(),
                    permissionRequest.getRequestedEngineName(),
                    permissionRequest.getRequestedPermission().getPermission()))
        );
        
        return receivedPermissionRequests;
    }
    
    public void createPermissionRequest(String requestedPermission, String engineName, String sender) {
        this.permissionRequests.add(PermissionRequestInOwner.create(
                PermissionType.valueOf(requestedPermission), PermissionStatus.PENDING, engineName, sender));
    }
}
