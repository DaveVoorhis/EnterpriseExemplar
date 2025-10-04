package org.reldb.exemplars.java.backend.model.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.reldb.exemplars.java.backend.model.ModelTestBase;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUser extends ModelTestBase {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void hasSettersAndGetters(boolean enabled) {
        final var user = new User();

        final var when = Instant.now();
        final var email = "test@example.com";

        user.setEmail(email);
        user.setEnabled(enabled);
        user.setLastLogin(when);

        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.isEnabled()).isEqualTo(enabled);
        assertThat(user.getLastLogin()).isEqualTo(when);
    }

    @Test
    void emailColumnIsNotNullable() throws Exception {
        assertFieldIsNotNullable(User.class, "email");
    }

    @Test
    void lastLoginColumnIsNotNullable() throws Exception {
        assertFieldIsNotNullable(User.class, "lastLogin");
    }

    @Test
    void idIsAnnotatedWithIdAndGeneratedValue() throws Exception {
        assertFieldIsGeneratedSequenceId(User.class, "userId");
    }
}
