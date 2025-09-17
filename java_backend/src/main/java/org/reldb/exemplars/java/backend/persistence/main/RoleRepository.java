package org.reldb.exemplars.java.backend.persistence.main;

import org.reldb.exemplars.java.backend.model.main.Role;
import jakarta.transaction.Transactional;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<@NonNull Role, @NonNull Long> {
    // spotless:off
    @NativeQuery(value = """
            SELECT roles.*
              FROM user_roles, roles
             WHERE user_roles.user_id = :userId
               AND user_roles.role_id = roles.role_id
            """)
    // spotless:on
    List<Role> findRolesByUserId(long userId);

    @Transactional
    @Modifying
    // spotless:off
    @NativeQuery(value = """
            INSERT INTO roles (role_id, name, description, active)
            VALUES (:roleId, :name, :description, true);
            """)
    // spotless:on
    void addSpecialRole(long roleId, String name, String description);
}
