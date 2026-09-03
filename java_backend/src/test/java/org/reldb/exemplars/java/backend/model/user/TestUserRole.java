package org.reldb.exemplars.java.backend.model.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.reldb.exemplars.java.backend.model.ModelTestBase;

public class TestUserRole extends ModelTestBase {

    @Test
    void canCreate() {
        final var userRole = new UserRole();
        assertThat(userRole).isNotNull();
    }

    @Test
    void canCreateAndGetSpecifiedArgs() {
        final var userId = 2L;
        final var roleId = 23L;

        final var userRole = new UserRole(userId, roleId);

        assertThat(userRole.getUserId()).isEqualTo(userId);
        assertThat(userRole.getRoleId()).isEqualTo(roleId);
    }
}