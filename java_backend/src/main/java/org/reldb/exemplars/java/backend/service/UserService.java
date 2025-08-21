package org.reldb.exemplars.java.backend.service;

import org.reldb.exemplars.java.backend.enums.Permissions;
import org.reldb.exemplars.java.backend.exception.custom.*;
import org.reldb.exemplars.java.backend.model.main.*;
import org.reldb.exemplars.java.backend.persistence.main.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    public final static long ADMIN_ROLE_ID = 1;
    public final static long USER_ROLE_ID = 2;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserContextService userContextService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Value("${new-user-is-enabled-by-default:false}")
    private boolean newUserEnabledDefault;
    @Value("${first-user-is-admin:false}")
    private boolean firstUserIsAdmin;

    public User getCurrentUser() {
        final var username = userContextService.getUsername();
        return findUserByEmail(username);
    }

    public List<User> getAllUsers() {
        return Streamable.of(userRepository.findAll()).toList();
    }

    public User findUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    public User findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);
    }

    public void setEnabled(long userId, boolean enabled) {
        final var user = findUser(userId);
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    public boolean isCurrentUserAllowedTo(Permissions permissionRequired) {
        final var user = getCurrentUser();
        final var permissionName = permissionRequired.name();
        return rolePermissionRepository.countActiveEnabledRolePermission(user.getUserId(), permissionName) > 0;
    }

    public boolean isEnabled(long userId) {
        return findUser(userId).isEnabled();
    }

    // Synchronized to avoid race condition if frontend issues multiple requests
    // simultaneously, which will cause it to repeatedly create
    // new user if the user doesn't exist.
    //
    // Future work may wish to replace this with not creating new user if it
    // doesn't exist, and require separate call to request new user creation,
    // and then only creation of new user need be synchronized.
    public synchronized User login(String userEmail) {
        log.debug("User %s requests login.".formatted(userEmail));
        final var maybeUser = userRepository.findByEmail(userEmail);
        if (maybeUser.isEmpty()) {
            final var user = addUser(userEmail);
            log.debug("User %s added and assigned id %s.".formatted(userEmail, user.getUserId()));
            grantRoleToUser(user.getUserId(), USER_ROLE_ID);
            if (firstUserIsAdmin && userRepository.count() == 1) {
                grantRoleToUser(user.getUserId(), ADMIN_ROLE_ID);
            }
            return user;
        } else {
            final var user = maybeUser.get();
            user.setLastLogin(Instant.now());
            log.debug("User %s last login updated.".formatted(userEmail));
            return userRepository.save(user);
        }
    }

    public List<Permissions> getAllPermissions() {
        return Arrays.asList(Permissions.values());
    }

    public List<String> getCurrentUserPermissions() {
        return rolePermissionRepository.findActiveUserPermissions(getCurrentUser().getUserId());
    }

    public List<Role> getUserRoles(long userId) {
        return roleRepository.findRolesByUserId(userId);
    }

    public void grantRoleToUser(long userId, long roleId) {
        checkUserExists(userId);
        checkRoleExists(roleId);
        if (userRoleRepository.existsById(new UserRolePK(userId, roleId))) {
            return;
        }
        final var userRole = new UserRole(userId, roleId);
        userRoleRepository.save(userRole);
        log.debug("User role %s added to user %s.".formatted(roleId, userId));
    }

    public void revokeRoleFromUser(long userId, long roleId) {
        checkUserExists(userId);
        checkRoleExists(roleId);
        userRoleRepository.deleteById(new UserRolePK(userId, roleId));
    }

    public List<Role> getAllRoles() {
        return Streamable.of(roleRepository.findAll()).toList();
    }

    public void deleteRole(long roleId) {
        checkRoleExists(roleId);
        if (roleId == ADMIN_ROLE_ID) {
            throw new RoleDeletionException(ADMIN_ROLE_ID);
        } else if (roleId == USER_ROLE_ID) {
            throw new RoleDeletionException(USER_ROLE_ID);
        }
        if (userRoleRepository.existsByRoleId(roleId)) {
            throw new RoleInUseMayNotBeDeletedException(roleId);
        }
        final var role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(roleId));
        roleRepository.delete(role);
    }

    public Role addRole(String name, String description, boolean active) {
        return saveRoleAs(new Role(), name, description, active);
    }

    public Role updateRole(long roleId, String name, String description, boolean active) {
        final var role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(roleId));
        return saveRoleAs(role, name, description, active);
    }

    public List<Permissions> getRolePermissions(long roleId) {
        final var rolePermissions = rolePermissionRepository.findRolePermissions(roleId);
        return rolePermissions.stream()
                .map(this::toPermissions)
                .filter(Objects::nonNull)
                .toList();
    }

    public void grantPermissionToRole(long roleId, Permissions permission) {
        checkForImmutableRolePermissions(roleId);
        checkRoleExists(roleId);
        final var permissionName = permission.name();
        if (rolePermissionRepository.existsById(new RolePermissionPK(roleId, permissionName))) {
            return;
        }
        final var rolePermission = new RolePermission();
        rolePermission.setRoleId(roleId);
        rolePermission.setPermissionName(permissionName);
        rolePermissionRepository.save(rolePermission);
    }

    public void revokePermissionFromRole(long roleId, Permissions permission) {
        checkForImmutableRolePermissions(roleId);
        checkRoleExists(roleId);
        rolePermissionRepository.deleteById(new RolePermissionPK(roleId, permission.name()));
    }

    private void checkForImmutableRolePermissions(long roleId) {
        if (roleId == ADMIN_ROLE_ID) {
            throw new RoleImmutableException(roleId);
        }
    }

    private void checkRoleExists(long roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new RoleNotFoundException(roleId);
        }
    }

    private void checkUserExists(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
    }

    private Permissions toPermissions(RolePermission rolePermission) {
        try {
            return Permissions.valueOf(rolePermission.getPermissionName());
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }

    private Role saveRoleAs(Role role, String name, String description, boolean active) {
        role.setName(name);
        role.setDescription(description);
        role.setActive(active);
        return roleRepository.save(role);
    }

    private User addUser(String userEmail) {
        final var user = new User();
        user.setEmail(userEmail);
        user.setEnabled(newUserEnabledDefault);
        user.setLastLogin(Instant.now());
        return userRepository.save(user);
    }
}
