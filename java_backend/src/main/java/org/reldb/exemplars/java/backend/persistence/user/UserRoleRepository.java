package org.reldb.exemplars.java.backend.persistence.user;

import org.reldb.exemplars.java.backend.model.user.UserRole;
import org.reldb.exemplars.java.backend.model.user.UserRolePK;
import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<@NonNull UserRole, @NonNull UserRolePK> {
    boolean existsByRoleId(long roleId);
}
