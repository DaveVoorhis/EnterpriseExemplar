package org.reldb.exemplars.java.backend.api.service;

import static org.reldb.exemplars.java.backend.enums.Permissions.FIND_USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.reldb.exemplars.java.backend.api.mappers.PermissionMapper;
import org.reldb.exemplars.java.backend.api.mappers.UserMapper;
import org.reldb.exemplars.java.backend.api.model.UserSetEnabledIn;
import org.reldb.exemplars.java.backend.enums.Permissions;
import org.reldb.exemplars.java.backend.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
class TestUserServiceAdapter {

    @Mock
    private UserService service;

    @InjectMocks
    private UserServiceAdapter adapter;

    @Autowired
    @Spy
    private UserMapper userMapper;

    @Autowired
    @Spy
    private PermissionMapper permissionMapper;

    @Test
    void verifyGetAllUsers() {
        adapter.getAllUsers();

        verify(service, times(1)).getAllUsers();
    }

    @Test
    void verifyFindUser() {
        final long userId = 123;

        adapter.findUser(userId);

        verify(service, times(1)).findUser(eq(userId));
    }

    @Test
    void verifySetEnabled() {
        final long userId = 123;

        adapter.setEnabled(userId, new UserSetEnabledIn(true));
        verify(service, times(1)).setEnabled(eq(userId), eq(true));

        adapter.setEnabled(userId, new UserSetEnabledIn(false));
        verify(service, times(1)).setEnabled(eq(userId), eq(false));
    }

    @Test
    void verifyIsCurrentUserAllowedTo() {
        final Permissions permission = FIND_USER;
        final String permissionName = permission.name();

        adapter.isCurrentUserAllowedTo(permissionName);

        verify(service, times(1)).isCurrentUserAllowedTo(permission);
    }

    @Test
    void verifyGetCurrentUserPermissions() {
        adapter.getCurrentUserPermissions();

        verify(service, times(1)).getCurrentUserPermissions();
    }

    @Test
    @DisplayName("Should return false for bogus permission names to prevent leaking name validity.")
    void verifyIsCurrentUserAllowedToDoesNotLeakPermissionNameValidity() {
        String bogusPermissionName = "BoGuSPermission";

        final Boolean result = adapter.isCurrentUserAllowedTo(bogusPermissionName);

        verify(service, times(0)).isCurrentUserAllowedTo(any());
        assertThat(result).isFalse();
    }
}
