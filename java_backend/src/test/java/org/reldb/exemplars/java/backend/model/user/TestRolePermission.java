package org.reldb.exemplars.java.backend.model.user;

import org.junit.jupiter.api.Test;
import org.reldb.exemplars.java.backend.model.ModelTestBase;

import static org.assertj.core.api.Assertions.assertThat;

public class TestRolePermission extends ModelTestBase {

    @Test
    void hasSettersAndGetters() {
        final var rolePermission = new RolePermission();

        final var permissionName = "test";
        final var roleId = 2L;

        rolePermission.setPermissionName(permissionName);
        rolePermission.setRoleId(roleId);

        assertThat(rolePermission.getPermissionName()).isEqualTo(permissionName);
        assertThat(rolePermission.getRoleId()).isEqualTo(roleId);
    }
}