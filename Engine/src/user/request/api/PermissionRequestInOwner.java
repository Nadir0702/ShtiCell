package user.request.api;

import user.permission.PermissionStatus;
import user.permission.PermissionType;
import user.request.impl.PermissionRequest;

public interface PermissionRequestInOwner {
    String getRequestedEngineName();
    String getRequestSenderUserName();
    PermissionType getRequestedPermission();
    PermissionStatus getRequestStatus();
    static PermissionRequestInOwner create(PermissionType requestedPermission,
                                                            PermissionStatus requestStatus,
                                                            String requestedEngineName,
                                                            String requestSenderUserName){
        return new PermissionRequest(
                requestedPermission,
                requestStatus,
                requestedEngineName,
                requestSenderUserName);
    }
}
