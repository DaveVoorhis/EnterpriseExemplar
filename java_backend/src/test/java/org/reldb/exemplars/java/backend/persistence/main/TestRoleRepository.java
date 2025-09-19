package org.reldb.exemplars.java.backend.persistence.main;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.reldb.exemplars.java.backend.ApplicationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestRoleRepository extends ApplicationTestBase {
    @Autowired
    private RoleRepository repository;

    @Test
    void canFindAllRoles() {
        final var roles = repository.findAll();

        assertThat(roles).hasSize(2);

        final var iterator = roles.iterator();

        final var role0 = iterator.next();
        assertThat(role0.getRoleId()).isEqualTo(1);
        assertThat(role0.getName()).isEqualTo("ADMIN");
        assertThat(role0.getDescription()).isEqualTo("Administrator");
        assertThat(role0.isActive()).isTrue();

        final var role1 = iterator.next();
        assertThat(role1.getRoleId()).isEqualTo(2);
        assertThat(role1.getName()).isEqualTo("USER");
        assertThat(role1.getDescription()).isEqualTo("User");
        assertThat(role1.isActive()).isTrue();
    }
}
