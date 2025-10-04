package org.reldb.exemplars.java.backend.service;

import static org.reldb.exemplars.java.backend.service.UserService.ADMIN_ROLE_ID;
import static org.reldb.exemplars.java.backend.service.UserService.USER_ROLE_ID;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.reldb.exemplars.java.backend.ApplicationTestBase;
import org.reldb.exemplars.java.backend.enums.Permissions;
import org.reldb.exemplars.java.backend.exception.custom.*;
import org.reldb.exemplars.java.backend.model.user.User;
import org.reldb.exemplars.java.backend.model.user.UserRolePK;
import org.reldb.exemplars.java.backend.persistence.user.UserRepository;
import org.reldb.exemplars.java.backend.persistence.user.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class TestUserService extends ApplicationTestBase {

    private static final long VALID_ENABLED_USER_ID = 5;
    private static final String VALID_ENABLED_USER_EMAIL = "blah.blah@reldb.org";
    private static final long VALID_DISABLED_USER_ID = 6;
    private static final long INVALID_USER_ID = 9999;
    private static final String NEW_USER_EMAIL = "dvoorhis@reldb.org";
    private static final long VALID_ENABLED_USER_ID_2 = 7;
    private static final long INVALID_ROLE_ID = 9999;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @MockitoBean
    private UserContextService userContextService;

    @Test
    void verifyGetAllUsers() {
        final var users = userService.getAllUsers();

        assertThat(users).size().isEqualTo(4);
    }

    @Test
    void verifyFindUserByEmail() {
        final var user = userService.findUserByEmail(VALID_ENABLED_USER_EMAIL);
        assertThat(user.getUserId()).isEqualTo(VALID_ENABLED_USER_ID);
    }

    @Test
    void findUserHappyPath() {
        assertDoesNotThrow(() -> {
            final var user = userService.findUser(VALID_ENABLED_USER_ID);
            assertThat(user.getEmail()).isEqualTo(VALID_ENABLED_USER_EMAIL);
        });
    }

    @Test
    void findUserThrowsExceptionForNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.findUser(INVALID_USER_ID));
    }

    @Test
    void disableEnableUserHappyPath() {
        final var userId = VALID_ENABLED_USER_ID;
        assertThat(userService.findUser(userId).isEnabled()).isTrue();

        userService.setEnabled(userId, false);
        final var shouldBeDisabledUser = userService.findUser(userId);
        assertThat(shouldBeDisabledUser.isEnabled()).isFalse();

        userService.setEnabled(userId, true);
        final var shouldBeEnabledUser = userService.findUser(userId);
        assertThat(shouldBeEnabledUser.isEnabled()).isTrue();
    }

    @Test
    void enableDisableUserHappyPath() {
        final var userId = VALID_DISABLED_USER_ID;
        assertThat(userService.findUser(userId).isEnabled()).isFalse();

        userService.setEnabled(userId, true);
        final var shouldBeEnabledUser = userService.findUser(userId);
        assertThat(shouldBeEnabledUser.isEnabled()).isTrue();

        userService.setEnabled(userId, false);
        final var shouldBeDisabledUser = userService.findUser(userId);
        assertThat(shouldBeDisabledUser.isEnabled()).isFalse();
    }

    @Test
    void disableUserThrowsExceptionForUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.setEnabled(INVALID_USER_ID, true));
    }

    @Test
    void enableUserThrowsExceptionForUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.setEnabled(INVALID_USER_ID, true));
    }

    @Test
    void isEnabledHappyPath() {
        assertThat(userService.isEnabled(VALID_ENABLED_USER_ID)).isTrue();
        assertThat(userService.isEnabled(VALID_DISABLED_USER_ID)).isFalse();
    }

    @Test
    void isEnabledThrowsExceptionForUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.isEnabled(INVALID_USER_ID));
    }

    @Test
    void canLoginExistingUser() {
        final var userToLogin = userService.findUser(VALID_ENABLED_USER_ID);
        final var lastLogin = userToLogin.getLastLogin();
        final var userLoggedIn = userService.login(VALID_ENABLED_USER_EMAIL);
        assertThat(userLoggedIn.getLastLogin()).isAfterOrEqualTo(lastLogin);
    }

    @Test
    void canLoginNewUser() {
        when(userContextService.getUsername())
                .thenReturn(VALID_ENABLED_USER_EMAIL);
        final var newUserToLogin = userService.login(NEW_USER_EMAIL);
        final var newUserFromDatabase = userService.findUser(newUserToLogin.getUserId());
        assertThat(newUserFromDatabase).isNotNull();
        assertThat(newUserFromDatabase.getEmail()).isEqualTo(NEW_USER_EMAIL);
        assertThatNewUserIsGrantedUserRole(newUserFromDatabase);
        userRepository.delete(newUserToLogin);
    }

    @Test
    void verifyUserHasExpectedPermission() {
        when(userContextService.getUsername())
                .thenReturn(VALID_ENABLED_USER_EMAIL);
        assertThat(userService.isCurrentUserAllowedTo(Permissions.GET_ALL_DEMOS)).isTrue();
        assertThat(userService.isCurrentUserAllowedTo(Permissions.ADMIN)).isFalse();
    }

    @Test
    void verifyCurrentUserPermissions() {
        when(userContextService.getUsername())
                .thenReturn(VALID_ENABLED_USER_EMAIL);
        final var permissionNames = userService.getCurrentUserPermissions();
        assertThat(permissionNames).hasSize(2);
        assertThat(permissionNames).contains("GET_ALL_DEMOS", "ADD_DEMO");
    }

    @Test
    void canAddAndRemoveRole() {
        final var initialRoles = userService.getAllRoles();
        final var addedRole = userService.addRole("newrole", "new role", true);

        final var afterAddRoles = userService.getAllRoles();

        assertThat(afterAddRoles.size()).isEqualTo(initialRoles.size() + 1);

        userService.deleteRole(addedRole.getRoleId());

        final var afterDeleteRoles = userService.getAllRoles();

        assertThat(afterDeleteRoles).hasSameSizeAs(initialRoles);
    }

    @Test
    void canGrantAndRevokeRolePermission() {
        final var testRole = USER_ROLE_ID;
        final var testPermission = Permissions.UPDATE_DEMO;

        final var initialPermissions = userService.getRolePermissions(testRole);

        userService.grantPermissionToRole(testRole, testPermission);
        assertDoesNotThrow(() -> userService.grantPermissionToRole(testRole, testPermission));

        final var afterGrantPermissions = userService.getRolePermissions(testRole);

        assertThat(afterGrantPermissions.size()).isEqualTo(initialPermissions.size() + 1);

        userService.revokePermissionFromRole(testRole, testPermission);
        assertDoesNotThrow(() -> userService.revokePermissionFromRole(testRole, testPermission));

        final var afterRevokePermissions = userService.getRolePermissions(testRole);

        assertThat(afterRevokePermissions).hasSameSizeAs(initialPermissions);
    }

    @Test
    void canGrantAndRevokeUserRole() {
        final var testUser = VALID_ENABLED_USER_ID_2;
        final var testRole = USER_ROLE_ID;

        final var initialRoles = userService.getUserRoles(testUser);

        userService.grantRoleToUser(testUser, testRole);
        assertDoesNotThrow(() -> userService.grantRoleToUser(testUser, testRole));

        final var afterGrantRole = userService.getUserRoles(testUser);

        assertThat(afterGrantRole.size()).isEqualTo(initialRoles.size() + 1);

        userService.revokeRoleFromUser(testUser, testRole);
        assertDoesNotThrow(() -> userService.revokeRoleFromUser(testUser, testRole));

        final var afterRevokeRole = userService.getUserRoles(testUser);

        assertThat(afterRevokeRole).hasSameSizeAs(initialRoles);
    }

    @Test
    void grantRoleToUserRequiresValidUser() {
        assertThrows(UserNotFoundException.class,
                () -> userService.grantRoleToUser(INVALID_USER_ID, USER_ROLE_ID));
    }

    @Test
    void grantRoleToUserRequiresValidRole() {
        assertThrows(RoleNotFoundException.class,
                () -> userService.grantRoleToUser(VALID_ENABLED_USER_ID, INVALID_ROLE_ID));
    }

    @Test
    void revokeRoleFromUserRequiresValidUser() {
        assertThrows(UserNotFoundException.class,
                () -> userService.revokeRoleFromUser(INVALID_USER_ID, USER_ROLE_ID));
    }

    @Test
    void revokeRoleFromUserRequiresValidRole() {
        assertThrows(RoleNotFoundException.class,
                () -> userService.revokeRoleFromUser(VALID_ENABLED_USER_ID, INVALID_ROLE_ID));
    }

    @Test
    void cannotDeleteSystemRoles() {
        assertThrows(RoleDeletionException.class,
                () -> userService.deleteRole(ADMIN_ROLE_ID));
        assertThrows(RoleDeletionException.class,
                () -> userService.deleteRole(USER_ROLE_ID));
    }

    @Test
    void cannotDeleteRoleAssignedToAUser() {
        final var initialRoles = userService.getAllRoles();
        final var addedRole = userService.addRole("newrole", "new role", true);

        assertThat(addedRole.getRoleId()).isNotZero();

        final var afterAddRoles = userService.getAllRoles();

        assertThat(afterAddRoles.size()).isEqualTo(initialRoles.size() + 1);

        userService.grantRoleToUser(VALID_ENABLED_USER_ID, addedRole.getRoleId());

        assertThrows(RoleInUseMayNotBeDeletedException.class,
                () -> userService.deleteRole(addedRole.getRoleId()));

        userService.revokeRoleFromUser(VALID_ENABLED_USER_ID, addedRole.getRoleId());

        userService.deleteRole(addedRole.getRoleId());

        final var afterDeleteRoles = userService.getAllRoles();

        assertThat(afterDeleteRoles).hasSameSizeAs(initialRoles);
    }

    @Test
    void cannotChangePermissionsForAdminRole() {
        assertThrows(RoleImmutableException.class,
                () -> userService.revokePermissionFromRole(ADMIN_ROLE_ID, Permissions.GET_ALL_DEMOS));
    }

    private void assertThatNewUserIsGrantedUserRole(User newUserFromDatabase) {
        final var userRole = userRoleRepository.findById(
                new UserRolePK(newUserFromDatabase.getUserId(), USER_ROLE_ID));
        assertThat(userRole).isPresent();
        assertThat(userService.isCurrentUserAllowedTo(Permissions.ADMIN)).isFalse();
    }
}
