package org.reldb.exemplars.java.backend.api.mappers;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.reldb.exemplars.java.backend.api.model.RoleOut;
import org.reldb.exemplars.java.backend.model.user.Role;
import java.util.concurrent.atomic.AtomicInteger;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

public class TestRoleMapper {
    private final RoleMapper mapper = Mappers.getMapper(RoleMapper.class);

    @ParameterizedTest
    @CsvSource({
            "1,blah,blahblah,true",
            "2,blat,zapzap,false"
    })
    void toRoleOutTest(long id, String name, String description, boolean active) {
        final var role = new Role();
        ReflectionTestUtils.setField(role, "roleId", id);
        role.setName(name);
        role.setDescription(description);
        role.setActive(active);

        final var roleOut = mapper.toRoleOut(role);

        assertMatch(role, roleOut);
    }

    @Test
    void toRolesOutTest() {
        final var roles = Instancio.ofList(Role.class).size(10).create();

        final var roleOuts = mapper.toRoleOut(roles);

        assertThat(roles).hasSameSizeAs(roleOuts);
        final var index = new AtomicInteger(0);
        roles.forEach(role -> assertMatch(role, roleOuts.get(index.getAndIncrement())));
    }

    private void assertMatch(Role role, RoleOut roleOut) {
        assertThat(role.getRoleId()).isEqualTo(roleOut.roleId());
        assertThat(role.getName()).isEqualTo(roleOut.name());
        assertThat(role.getDescription()).isEqualTo(roleOut.description());
        assertThat(role.isActive()).isEqualTo(roleOut.active());
    }
}
