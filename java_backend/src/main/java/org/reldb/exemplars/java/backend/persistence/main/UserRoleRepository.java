package org.reldb.exemplars.java.backend.persistence.main;

import org.reldb.exemplars.java.backend.model.main.UserRole;
import org.reldb.exemplars.java.backend.model.main.UserRolePK;
import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<@NonNull UserRole, @NonNull UserRolePK> {
    boolean existsByRoleId(long roleId);
}
