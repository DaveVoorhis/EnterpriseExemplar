package org.reldb.exemplars.java.backend.api.mappers;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.reldb.exemplars.java.backend.api.model.PermissionOut;
import org.reldb.exemplars.java.backend.enums.Permissions;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class TestPermissionMapper {
    private final PermissionMapper mapper = Mappers.getMapper(PermissionMapper.class);

    @Test
    void permissionsToPermissionOutTest() {
        final var permissions = Arrays.stream(Permissions.values()).toList();

        final var permissionOuts = mapper.toPermissionOut(permissions);

        assertThat(permissions).hasSameSizeAs(Permissions.values());
        assertThat(permissionOuts).hasSameSizeAs(permissions);
        final var index = new AtomicInteger(0);
        permissions.forEach(permission -> assertMatch(permission, permissionOuts.get(index.getAndIncrement())));
    }

    @Test
    void permissionNameToPermissionTest() {
        final var permissionNames = Permissions.names();

        assertThat(permissionNames).hasSameSizeAs(Permissions.values());

        permissionNames.forEach(permissionName -> assertThat(mapper.permissionNameToPermission(permissionName).name())
                .isEqualTo(permissionName));
    }

    @Test
    void permissionNameListToPermissionsTest() {
        final var permissionNames = Permissions.names();

        assertThat(permissionNames).hasSameSizeAs(Permissions.values());

        final var permissions = mapper.permissionNameToPermission(permissionNames);

        assertThat(permissions).hasSameSizeAs(permissionNames);
        final var index = new AtomicInteger(0);
        permissionNames.forEach(name -> assertThat(name).isEqualTo(permissionNames.get(index.getAndIncrement())));
    }

    private void assertMatch(Permissions permission, PermissionOut permissionOut) {
        assertThat(permission.name()).isEqualTo(permissionOut.name());
        assertThat(permission.getCategory()).isEqualTo(permissionOut.category());
        assertThat(permission.getDescription()).isEqualTo(permissionOut.description());
    }
}
