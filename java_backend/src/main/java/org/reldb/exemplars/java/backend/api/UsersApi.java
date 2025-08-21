package org.reldb.exemplars.java.backend.api;

import org.reldb.exemplars.java.backend.api.interceptors.users.Permit;
import org.reldb.exemplars.java.backend.api.model.PermissionOut;
import org.reldb.exemplars.java.backend.api.model.RoleIn;
import org.reldb.exemplars.java.backend.api.model.RoleOut;
import org.reldb.exemplars.java.backend.api.model.UserOut;
import org.reldb.exemplars.java.backend.api.model.UserSetEnabledIn;
import org.reldb.exemplars.java.backend.api.service.UserServiceAdapter;
import org.reldb.exemplars.java.backend.enums.Permissions;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("/users")
@RestController
public class UsersApi extends ApiDefault {

    @Autowired
    private UserServiceAdapter userService;

    @Permit(Permissions.ADMIN)
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull List<UserOut>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Permit(Permissions.FIND_USER)
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull UserOut> findUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.findUser(userId));
    }

    @Permit(Permissions.ADMIN)
    @PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull Void> setEnabled(@PathVariable long userId, @RequestBody UserSetEnabledIn setEnabledIn) {
        userService.setEnabled(userId, setEnabledIn);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull UserOut> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @GetMapping(value = "/permission/{permissionName}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull Boolean> hasPermission(@PathVariable String permissionName) {
        return ResponseEntity.ok(userService.isCurrentUserAllowedTo(permissionName));
    }

    @Permit(Permissions.ADMIN)
    @GetMapping(value = "/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull List<PermissionOut>> getAllPermissions() {
        return ResponseEntity.ok(userService.getAllPermissions());
    }

    @GetMapping(value = "/mypermissions", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull List<PermissionOut>> getCurrentUserPermissions() {
        return ResponseEntity.ok(userService.getCurrentUserPermissions());
    }

    @Permit(Permissions.ADMIN)
    @GetMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull List<RoleOut>> getAllRoles() {
        return ResponseEntity.ok(userService.getAllRoles());
    }

    @Permit(Permissions.ADMIN)
    @DeleteMapping(value = "/roles/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull Void> deleteRole(@PathVariable long roleId) {
        userService.deleteRole(roleId);
        return ResponseEntity.ok().build();
    }

    @Permit(Permissions.ADMIN)
    @PostMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull RoleOut> addRole(@RequestBody RoleIn role) {
        return ResponseEntity.ok(userService.addRole(role));
    }

    @Permit(Permissions.ADMIN)
    @PutMapping(value = "/roles/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull RoleOut> updateRole(@PathVariable long roleId, @RequestBody RoleIn role) {
        return ResponseEntity.ok(userService.updateRole(roleId, role));
    }

    @Permit(Permissions.ADMIN)
    @GetMapping(value = "/{userId}/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull List<RoleOut>> getUserRoles(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUserRoles(userId));
    }

    @Permit(Permissions.ADMIN)
    @PostMapping(value = "/{userId}/roles/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull Void> grantRoleToUser(@PathVariable long userId, @PathVariable long roleId) {
        userService.grantRoleToUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @Permit(Permissions.ADMIN)
    @DeleteMapping(value = "/{userId}/roles/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull Void> revokeRoleFromUser(@PathVariable long userId, @PathVariable long roleId) {
        userService.revokeRoleFromUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @Permit(Permissions.ADMIN)
    @GetMapping(value = "/role/{roleId}/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull List<PermissionOut>> getRolePermissions(@PathVariable long roleId) {
        return ResponseEntity.ok(userService.getRolePermissions(roleId));
    }

    @Permit(Permissions.ADMIN)
    @PostMapping(value = "/role/{roleId}/permissions/{permissionName}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull Void> grantRole(@PathVariable long roleId, @PathVariable String permissionName) {
        userService.grantPermissionToRole(roleId, permissionName);
        return ResponseEntity.ok().build();
    }

    @Permit(Permissions.ADMIN)
    @DeleteMapping(value = "/role/{roleId}/permissions/{permissionName}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull Void> revokeRole(@PathVariable long roleId, @PathVariable String permissionName) {
        userService.revokePermissionFromRole(roleId, permissionName);
        return ResponseEntity.ok().build();
    }
}