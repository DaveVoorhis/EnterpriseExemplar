package org.reldb.exemplars.java.backend.persistence.user;

import org.reldb.exemplars.java.backend.model.user.User;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<@NonNull User, @NonNull Long> {
    Optional<User> findByEmail(String userEmail);
}
