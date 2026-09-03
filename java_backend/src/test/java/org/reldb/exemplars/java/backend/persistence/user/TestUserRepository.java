package org.reldb.exemplars.java.backend.persistence.user;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import lombok.RequiredArgsConstructor;
import org.reldb.exemplars.java.backend.ApplicationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class TestUserRepository extends ApplicationTestBase {
    private final UserRepository repository;

    private static final String VALID_ENABLED_USER_EMAIL = "blah.blah@reldb.org";

    @Test
    void canFindAllUsers() {
        final var users = repository.findAll();
        assertThat(users).hasSize(4);
    }

    @Test
    void canFindByEmail() {
        final var user = repository.findByEmail(VALID_ENABLED_USER_EMAIL);
        assertThat(user).isPresent();
    }
}
