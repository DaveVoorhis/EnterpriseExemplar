package org.reldb.exemplars.java.backend.service;

import static org.reldb.exemplars.java.backend.service.UserService.ADMIN_ROLE_ID;
import static org.reldb.exemplars.java.backend.service.UserService.USER_ROLE_ID;

import org.reldb.exemplars.java.backend.enums.Permissions;
import org.reldb.exemplars.java.backend.model.main.RolePermission;
import org.reldb.exemplars.java.backend.model.main.RolePermissionPK;
import org.reldb.exemplars.java.backend.persistence.main.RolePermissionRepository;
import org.reldb.exemplars.java.backend.persistence.main.RoleRepository;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartupService implements ApplicationListener<@NonNull ApplicationReadyEvent> {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Override
    public void onApplicationEvent(@NonNull final ApplicationReadyEvent event) {
        ensureAdminRoleExistsAndHasAllPermissions();
        ensureUserRoleExists();
    }

    private void ensureAdminRoleExistsAndHasAllPermissions() {
        ensureRoleExists(ADMIN_ROLE_ID, "ADMIN", "Administrator role");
        final var newRolePermissions = Arrays.stream(Permissions.values())
                .filter(permission -> !rolePermissionRepository.existsById(
                        new RolePermissionPK(ADMIN_ROLE_ID, permission.name())))
                .map(this::newAdminRolePermission)
                .toList();
        rolePermissionRepository.saveAll(newRolePermissions);
        rolePermissionRepository.deletePermissionsNotIn(Permissions.names());
    }

    private RolePermission newAdminRolePermission(Permissions permission) {
        final var rolePermission = new RolePermission();
        rolePermission.setRoleId(ADMIN_ROLE_ID);
        rolePermission.setPermissionName(permission.name());
        return rolePermission;
    }

    private void ensureUserRoleExists() {
        ensureRoleExists(USER_ROLE_ID, "USER", "Default user role");
    }

    private synchronized void ensureRoleExists(long roleId, String name, String description) {
        if (!roleRepository.existsById(roleId)) {
            roleRepository.addSpecialRole(roleId, name, description);
        }
    }
}
