package org.reldb.exemplars.java.backend.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.reldb.exemplars.java.backend.api.interceptors.auth.MockSecureUser;
import org.reldb.exemplars.java.backend.api.model.*;
import org.reldb.exemplars.java.backend.enums.Permissions;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Arrays;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

class TestUsersApi extends ApiTestBase {
    @Autowired
    MockSecureUser secureUser;

    // Current user set for tests by noauth profile
    private static final long CURRENT_USER_ID = 4;

    private static final String USERS_URL = "/users";
    private static final String USER_URL = USERS_URL + "/%d";

    private static final long VALID_ENABLED_USER_ID = 5;
    private static final String VALID_ENABLED_USER_EMAIL = "blah.blah@reldb.org";
    private static final long VALID_DISABLED_USER_ID = 6;

    private static final long ADMIN_ROLE = 1;
    private static final long VALID_ENABLED_USER_ID_2 = 7;

    @Test
    void shouldReturnListOfUsers() {
        final var url = getBaseUrl() + USERS_URL;

        final ResponseEntity<@NonNull List<UserOut>> response = get(url, new ParameterizedTypeReference<>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().get(X_REQUEST_ID)).isNotNull();
        final List<UserOut> users = response.getBody();
        assertThat(users).hasSize(4);
    }

    @Test
    void shouldFetchValidUser() {
        final var user = findUser(VALID_ENABLED_USER_ID);
        assertThat(user).isNotNull();
        assertThat(user.userId()).isEqualTo(VALID_ENABLED_USER_ID);
        assertThat(user.email()).isEqualTo(VALID_ENABLED_USER_EMAIL);
        assertThat(user.enabled()).isTrue();
    }

    @Test
    void shouldSetDisabledEnabled() throws JsonProcessingException {
        final var user = VALID_DISABLED_USER_ID;
        final var url = getBaseUrl() + USER_URL.formatted(user);

        verifySetEnabled(url, user, new UserSetEnabledIn(true));
        verifySetEnabled(url, user, new UserSetEnabledIn(false));
    }

    @Test
    void shouldSetEnabledDisabled() throws JsonProcessingException {
        final var user = VALID_ENABLED_USER_ID;
        final var url = getBaseUrl() + USER_URL.formatted(user);

        verifySetEnabled(url, user, new UserSetEnabledIn(false));
        verifySetEnabled(url, user, new UserSetEnabledIn(true));
    }

    @Test
    void shouldFetchCurrentUser() {
        final var url = getBaseUrl() + USERS_URL + "/current";

        final ResponseEntity<@NonNull UserOut> response = get(url, new ParameterizedTypeReference<>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().get(X_REQUEST_ID)).isNotNull();

        final var user = response.getBody();

        assertThat(user).isNotNull();
        assertThat(user.userId()).isEqualTo(CURRENT_USER_ID);
        assertThat(user.email()).isEqualTo(secureUser.getUserid());
        assertThat(user.enabled()).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            "FIND_USER, true",
            "CHECK_PERMISSION, true",
            "no_such_permission, false",
            "ADMIN, true"
    })
    void shouldIndicateCurrentUserHasPermission(String permissionToCheck, boolean expectedResult) {
        final var url = getBaseUrl() + USERS_URL + "/permission/" + permissionToCheck;

        final ResponseEntity<@NonNull Boolean> response = get(url, new ParameterizedTypeReference<>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResult);
    }

    @Test
    void shouldReturnFalseForNonexistentPermission() {
        final var url = getBaseUrl() + USERS_URL + "/permission/SpLaT";

        final ResponseEntity<@NonNull Boolean> response = get(url, new ParameterizedTypeReference<>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isFalse();
    }

    @Test
    void shouldGetCurrentUserPermissions() {
        final var url = getBaseUrl() + USERS_URL + "/mypermissions";

        final ResponseEntity<@NonNull List<PermissionOut>> response = get(url, new ParameterizedTypeReference<>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(Permissions.values().length);
        final var actualPermissionNames = response.getBody().stream()
                .map(PermissionOut::name)
                .toList();
        final var expectedPermissionNames = Arrays.stream(Permissions.values())
                .map(Enum::name)
                .toList();
        assertThat(actualPermissionNames).hasSameElementsAs(expectedPermissionNames);
    }

    @Test
    void shouldGetListOfAllRoles() {
        final var roles = getAllRoles();

        assertThat(roles).hasSize(2);
    }

    @Test
    void shouldAddUpdateAndDeleteARole() throws JsonProcessingException {
        final var initialRolesCount = getAllRoles().size();

        final var addedRole = addRole("blah", "blahblahblah", true);
        assertThat(getAllRoles()).hasSize(initialRolesCount + 1);

        final var updatedRole = updateRole(addedRole.roleId(), "zot", "zap", true);
        assertThat(getAllRoles()).hasSize(initialRolesCount + 1);

        removeRole(updatedRole.roleId());
        assertThat(getAllRoles()).hasSize(initialRolesCount);
    }

    @Test
    void shouldGetAllPermissions() {
        final var url = getBaseUrl() + USERS_URL + "/permissions";

        final ResponseEntity<@NonNull List<PermissionOut>> response = get(url, new ParameterizedTypeReference<>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final var body = response.getBody();
        assertThat(body).hasSize(Permissions.values().length);
        final var permissions = Arrays.stream(Permissions.values())
                .map(permission -> new PermissionOut(
                        permission.name(),
                        permission.getDescription(),
                        permission.getCategory()))
                .toList();
        assertThat(body).hasSameElementsAs(permissions);
    }

    @Test
    void shouldGetUserRoles() {
        final var userRoles = getUserRoles(VALID_ENABLED_USER_ID);

        assertThat(userRoles).hasSize(1);
        assertThat(userRoles.getFirst().name()).isEqualTo("USER");
    }

    @Test
    void shouldGrantAndRevokeUserRole() throws JsonProcessingException {
        final var testUserId = VALID_ENABLED_USER_ID_2;
        final var testRoleId = ADMIN_ROLE;
        final var initialUserRolesCount = getUserRoles(testUserId).size();

        grantRoleToUser(testUserId, testRoleId);
        assertThat(getUserRoles(testUserId)).hasSize(initialUserRolesCount + 1);

        revokeRoleFromUser(testUserId, testRoleId);
        assertThat(getUserRoles(testUserId)).hasSize(initialUserRolesCount);
    }

    @Test
    void shouldGetRolePermissions() {
        final var permissions = getRolePermissions(ADMIN_ROLE);

        assertThat(permissions).hasSize(Permissions.values().length);
    }

    @Test
    void shouldGrantAndRevokeRolePermission() throws JsonProcessingException {
        final var testPermission = Permissions.ADMIN;

        final var newRole = addRole("blah", "blahblah", true);
        final var initialRolePermissionCount = getRolePermissions(newRole.roleId()).size();
        assertThat(initialRolePermissionCount).isZero();

        grantPermissionToRole(newRole.roleId(), testPermission);
        assertThat(getRolePermissions(newRole.roleId())).hasSize(initialRolePermissionCount + 1);

        revokePermissionFromRole(newRole.roleId(), testPermission);
        assertThat(getRolePermissions(newRole.roleId())).hasSize(initialRolePermissionCount);

        removeRole(newRole.roleId());
        assertThat(getRolePermissions(newRole.roleId())).hasSize(initialRolePermissionCount);
    }

    private List<RoleOut> getAllRoles() {
        final var url = getBaseUrl() + USERS_URL + "/roles";

        final ResponseEntity<@NonNull List<RoleOut>> response = get(url, new ParameterizedTypeReference<>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        return response.getBody();
    }

    private RoleOut addRole(String name, String description, boolean active) throws JsonProcessingException {
        final var addURL = getBaseUrl() + USERS_URL + "/roles";

        final ResponseEntity<@NonNull RoleOut> addResponse = post(addURL,
                new ParameterizedTypeReference<>() {
                },
                new RoleIn(name, description, active));

        assertThat(addResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        final var addBody = addResponse.getBody();
        assertThat(addBody.name()).isEqualTo(name);
        assertThat(addBody.description()).isEqualTo(description);
        assertThat(addBody.active()).isEqualTo(active);

        return addBody;
    }

    private RoleOut updateRole(long roleId, String name, String description, boolean active)
            throws JsonProcessingException {
        final var updateUrl = getBaseUrl() + USERS_URL + "/roles/" + roleId;

        final ResponseEntity<@NonNull RoleOut> updateResponse = put(updateUrl,
                new ParameterizedTypeReference<>() {
                },
                new RoleIn("zot", "zap", true));

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        final var updateBody = updateResponse.getBody();
        assertThat(updateBody.roleId()).isEqualTo(roleId);
        assertThat(updateBody.name()).isEqualTo(name);
        assertThat(updateBody.description()).isEqualTo(description);
        assertThat(updateBody.active()).isEqualTo(active);
        return updateBody;
    }

    private void removeRole(long roleId) {
        final var deleteUrl = getBaseUrl() + USERS_URL + "/roles/" + roleId;

        final ResponseEntity<@NonNull Void> response = delete(deleteUrl, new ParameterizedTypeReference<>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private List<RoleOut> getUserRoles(long userId) {
        final var url = getBaseUrl() + USERS_URL + "/%s/roles".formatted(userId);

        final ResponseEntity<@NonNull List<RoleOut>> response = get(url, new ParameterizedTypeReference<>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        return response.getBody();
    }

    private void grantRoleToUser(long userId, long roleId) throws JsonProcessingException {
        final var addUrl = getBaseUrl() + USERS_URL + "/%s/roles/%s".formatted(userId, roleId);

        final ResponseEntity<@NonNull Void> addResponse = post(addUrl, new ParameterizedTypeReference<>() {
        }, null);

        assertThat(addResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void revokeRoleFromUser(long userId, long roleId) {
        final var deleteUrl = getBaseUrl() + USERS_URL + "/%s/roles/%s".formatted(userId, roleId);

        final ResponseEntity<@NonNull Void> deleteResponse = delete(deleteUrl, new ParameterizedTypeReference<>() {
        });

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private List<PermissionOut> getRolePermissions(long roleId) {
        final var url = getBaseUrl() + USERS_URL + "/role/%s/permissions".formatted(roleId);

        final ResponseEntity<@NonNull List<PermissionOut>> response = get(url, new ParameterizedTypeReference<>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        return response.getBody();
    }

    private void grantPermissionToRole(long roleId, Permissions permission) throws JsonProcessingException {
        final var grantUrl = getBaseUrl() + USERS_URL
                + "/role/%s/permissions/%s".formatted(roleId, permission.name());

        final ResponseEntity<@NonNull Void> grantResponse = post(grantUrl, new ParameterizedTypeReference<>() {
        }, null);

        assertThat(grantResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void revokePermissionFromRole(long roleId, Permissions permission) {
        final var revokeURL = getBaseUrl() + USERS_URL
                + "/role/%s/permissions/%s".formatted(roleId, permission.name());

        final ResponseEntity<@NonNull Void> revokeResponse = delete(revokeURL, new ParameterizedTypeReference<>() {
        });

        assertThat(revokeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void verifySetEnabled(String url, long userId, UserSetEnabledIn enabled) throws JsonProcessingException {
        final ResponseEntity<@NonNull Void> responseSetEnabled = put(url, enabled);

        assertThat(responseSetEnabled.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseSetEnabled.getHeaders().get(X_REQUEST_ID)).isNotNull();

        final var user = findUser(userId);
        assertThat(user).isNotNull();
        assertThat(user.userId()).isEqualTo(userId);
        assertThat(user.enabled()).isEqualTo(enabled.enabled());
    }

    private UserOut findUser(long userId) {
        final var url = getBaseUrl() + USER_URL.formatted(userId);

        final ResponseEntity<@NonNull UserOut> response = get(url, new ParameterizedTypeReference<>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().get(X_REQUEST_ID)).isNotNull();
        return response.getBody();
    }
}
