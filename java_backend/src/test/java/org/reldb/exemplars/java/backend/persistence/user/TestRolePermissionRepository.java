package org.reldb.exemplars.java.backend.persistence.user;

import org.reldb.exemplars.java.backend.ApplicationTestBase;
import org.reldb.exemplars.java.backend.enums.Permissions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestRolePermissionRepository extends ApplicationTestBase {
    @Autowired
    private RolePermissionRepository repository;

    @Test
    void hasPermissions() {
        final var permissions = repository.countActiveEnabledRolePermission(4, Permissions.FIND_USER.name());
        AssertionsForClassTypes.assertThat(permissions).isNotZero();
    }

    @Test
    void noPermission() {
        final var permissions = repository.countActiveEnabledRolePermission(6, Permissions.ADD_DEMO.name());
        AssertionsForClassTypes.assertThat(permissions).isZero();
    }
}
