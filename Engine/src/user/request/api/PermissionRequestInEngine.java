package user.request.api;

import user.permission.PermissionStatus;
import user.permission.PermissionType;
import user.request.impl.PermissionRequest;

public interface PermissionRequestInEngine {
    
    PermissionType getCurrentPermission();
    PermissionType getRequestedPermission();
    PermissionStatus getRequestStatus();
    
    static PermissionRequestInEngine create(PermissionType currentPermission,
                                            PermissionType requestedPermission,
                                            PermissionStatus requestStatus) {
        return new PermissionRequest(currentPermission, requestedPermission, requestStatus);
    }
    
}
