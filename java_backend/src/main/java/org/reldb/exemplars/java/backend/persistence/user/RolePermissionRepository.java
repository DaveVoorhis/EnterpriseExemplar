package org.reldb.exemplars.java.backend.persistence.user;

import org.reldb.exemplars.java.backend.model.user.RolePermission;
import org.reldb.exemplars.java.backend.model.user.RolePermissionPK;
import jakarta.transaction.Transactional;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

public interface RolePermissionRepository extends CrudRepository<@NonNull RolePermission, @NonNull RolePermissionPK> {
    // spotless:off
    @NativeQuery(value = """
            SELECT COUNT(*)
            FROM app_users, user_roles, roles, role_permissions
            WHERE app_users.user_id = :userid
              AND role_permissions.permission_name = :permissionName
              AND app_users.enabled
              AND app_users.user_id = user_roles.user_id
              AND user_roles.role_id = roles.role_id
              AND roles.role_id = role_permissions.role_id
              AND roles.active
            """)
    // spotless:on
    long countActiveEnabledRolePermission(long userid, String permissionName);

    // spotless:off
    @NativeQuery(value = """
            SELECT DISTINCT role_permissions.permission_name
              FROM user_roles, roles, role_permissions
             WHERE user_roles.role_id = roles.role_id
               AND user_roles.user_id = :userId
               AND roles.role_id = role_permissions.role_id
               AND roles.active
            """)
    // spotless:on
    List<String> findActiveUserPermissions(long userId);

    // spotless:off
    @NativeQuery(value = """
            SELECT *
              FROM role_permissions
             WHERE role_id = :roleId
            """)
    // spotless:on
    List<RolePermission> findRolePermissions(long roleId);

    // spotless:off
    @Transactional
    @Modifying
    @NativeQuery(value = """
            DELETE FROM role_permissions
                  WHERE permission_name NOT IN (:list)
            """)
    // spotless:on
    void deletePermissionsNotIn(List<String> list);
}
