package org.reldb.exemplars.java.backend.persistence.user;

import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.reldb.exemplars.java.backend.model.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<@NonNull User, @NonNull Long> {
    Optional<User> findByEmail(String userEmail);
}
