package user.request.impl;

import user.permission.PermissionStatus;
import user.permission.PermissionType;
import user.request.api.PermissionRequestInEngine;
import user.request.api.PermissionRequestInOwner;

import java.util.Objects;

public class PermissionRequest implements PermissionRequestInEngine, PermissionRequestInOwner {
    private PermissionType currentPermission;
    private PermissionType requestedPermission;
    private PermissionStatus requestStatus;
    private String requestedEngineName;
    private String requestSenderUserName;
    
    public PermissionRequest(PermissionType currentPermission,
                             PermissionType requestedPermission,
                             PermissionStatus requestStatus) {
        this.currentPermission = currentPermission;
        this.requestedPermission = requestedPermission;
        this.requestStatus = requestStatus;
    }
    
    public PermissionRequest(PermissionType requestedPermission,
                             PermissionStatus requestStatus,
                             String requestedEngineName,
                             String requestSenderUserName) {
        this.requestedPermission = requestedPermission;
        this.requestStatus = requestStatus;
        this.requestedEngineName = requestedEngineName;
        this.requestSenderUserName = requestSenderUserName;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionRequest that = (PermissionRequest) o;
        return currentPermission == that.currentPermission && requestedPermission == that.requestedPermission && requestStatus == that.requestStatus && Objects.equals(requestedEngineName, that.requestedEngineName) && Objects.equals(requestSenderUserName, that.requestSenderUserName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(currentPermission, requestedPermission, requestStatus, requestedEngineName, requestSenderUserName);
    }
    
    @Override
    public PermissionType getCurrentPermission() {
        return this.currentPermission;
    }
    
    @Override
    public String getRequestedEngineName() {
        return this.requestedEngineName;
    }
    
    @Override
    public String getRequestSenderUserName() {
        return this.requestSenderUserName;
    }
    
    @Override
    public PermissionType getRequestedPermission() { return this.requestedPermission; }
    
    @Override
    public PermissionStatus getRequestStatus() { return this.requestStatus; }
}
