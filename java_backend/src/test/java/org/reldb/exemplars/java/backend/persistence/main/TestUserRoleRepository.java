package org.reldb.exemplars.java.backend.persistence.main;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.reldb.exemplars.java.backend.ApplicationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestUserRoleRepository extends ApplicationTestBase {
    @Autowired
    private UserRoleRepository repository;

    @Test
    void canFindAllUserRoles() {
        final var userRoles = repository.findAll();

        assertThat(userRoles).hasSize(3);

        final var userRoleIterator = userRoles.iterator();

        final var userRole0 = userRoleIterator.next();
        assertThat(userRole0.getUserId()).isEqualTo(4);
        assertThat(userRole0.getRoleId()).isEqualTo(1);

        final var userRole1 = userRoleIterator.next();
        assertThat(userRole1.getUserId()).isEqualTo(5);
        assertThat(userRole1.getRoleId()).isEqualTo(2);

        final var userRole2 = userRoleIterator.next();
        assertThat(userRole2.getUserId()).isEqualTo(6);
        assertThat(userRole2.getRoleId()).isEqualTo(1);
    }
}
