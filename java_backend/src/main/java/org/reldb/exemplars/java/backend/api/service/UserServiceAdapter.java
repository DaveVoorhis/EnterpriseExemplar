package org.reldb.exemplars.java.backend.api.service;

import org.reldb.exemplars.java.backend.api.mappers.PermissionMapper;
import org.reldb.exemplars.java.backend.api.mappers.RoleMapper;
import org.reldb.exemplars.java.backend.api.mappers.UserMapper;
import org.reldb.exemplars.java.backend.api.model.PermissionOut;
import org.reldb.exemplars.java.backend.api.model.RoleIn;
import org.reldb.exemplars.java.backend.api.model.RoleOut;
import org.reldb.exemplars.java.backend.api.model.UserOut;
import org.reldb.exemplars.java.backend.api.model.UserSetEnabledIn;
import org.reldb.exemplars.java.backend.enums.Permissions;
import org.reldb.exemplars.java.backend.exception.custom.PermissionNotFoundException;
import org.reldb.exemplars.java.backend.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceAdapter {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RoleMapper roleMapper;

    public UserOut getCurrentUser() {
        return userMapper.userToUserOut(userService.getCurrentUser());
    }

    public List<UserOut> getAllUsers() {
        return userMapper.userToUserOut(userService.getAllUsers());
    }

    public UserOut findUser(long userId) {
        return userMapper.userToUserOut(userService.findUser(userId));
    }

    public void setEnabled(long userId, UserSetEnabledIn enabled) {
        userService.setEnabled(userId, enabled.enabled());
    }

    public UserOut login(String username) {
        return userMapper.userToUserOut(userService.login(username));
    }

    public Boolean isCurrentUserAllowedTo(String permissionName) {
        final Permissions permission;
        try {
            permission = permissionMapper.permissionNameToPermission(permissionName);
        } catch (PermissionNotFoundException pnfe) {
            return false;
        }
        return isCurrentUserAllowedTo(permission);
    }

    public boolean isCurrentUserAllowedTo(Permissions permission) {
        return userService.isCurrentUserAllowedTo(permission);
    }

    public List<PermissionOut> getAllPermissions() {
        return permissionMapper.toPermissionOut(userService.getAllPermissions());
    }

    public List<PermissionOut> getCurrentUserPermissions() {
        return permissionMapper.toPermissionOut(
                permissionMapper.permissionNameToPermission(
                        userService.getCurrentUserPermissions()));
    }

    public List<RoleOut> getAllRoles() {
        return roleMapper.toRoleOut(userService.getAllRoles());
    }

    public void deleteRole(long roleId) {
        userService.deleteRole(roleId);
    }

    public RoleOut addRole(RoleIn role) {
        return roleMapper.toRoleOut(userService.addRole(role.name(), role.description(), role.active()));
    }

    public RoleOut updateRole(long roleId, RoleIn role) {
        return roleMapper.toRoleOut(userService.updateRole(roleId, role.name(), role.description(), role.active()));
    }

    public List<RoleOut> getUserRoles(long userId) {
        return roleMapper.toRoleOut(userService.getUserRoles(userId));
    }

    public void grantRoleToUser(long userId, long roleId) {
        userService.grantRoleToUser(userId, roleId);
    }

    public void revokeRoleFromUser(long userId, long roleId) {
        userService.revokeRoleFromUser(userId, roleId);
    }

    public List<PermissionOut> getRolePermissions(long roleId) {
        return permissionMapper.toPermissionOut(userService.getRolePermissions(roleId));
    }

    public void grantPermissionToRole(long roleId, String permissionName) {
        userService.grantPermissionToRole(roleId, permissionMapper.permissionNameToPermission(permissionName));
    }

    public void revokePermissionFromRole(long roleId, String permissionName) {
        userService.revokePermissionFromRole(roleId, permissionMapper.permissionNameToPermission(permissionName));
    }
}
