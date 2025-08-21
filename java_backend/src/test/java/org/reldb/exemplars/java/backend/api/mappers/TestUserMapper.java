package org.reldb.exemplars.java.backend.api.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.reldb.exemplars.java.backend.api.model.UserOut;
import org.reldb.exemplars.java.backend.model.main.User;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class TestUserMapper {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void userToUserOutTest() {
        final var user = Instancio.of(User.class).create();

        final var userOut = mapper.userToUserOut(user);

        assertMatch(user, userOut);
    }

    @Test
    void verifyNullUser() {
        assertThat(mapper.userToUserOut((User) null)).isNull();
    }

    @Test
    void usersToUserOutsTest() {
        final var users = Instancio.ofList(User.class).size(10).create();

        final var userOuts = mapper.userToUserOut(users);

        assertThat(users).hasSameSizeAs(userOuts);
        final var index = new AtomicInteger(0);
        users.forEach(user -> assertMatch(user, userOuts.get(index.getAndIncrement())));
    }

    @Test
    void verifyNullUsers() {
        assertThat(mapper.userToUserOut((List<User>) null)).isNull();
    }

    void assertMatch(@NotNull User user, @NotNull UserOut userOut) {
        assertThat(userOut.userId()).isEqualTo(user.getUserId());
        assertThat(userOut.email()).isEqualTo(user.getEmail());
        assertThat(userOut.lastLogin()).isEqualTo(user.getLastLogin());
        assertThat(userOut.enabled()).isEqualTo(user.isEnabled());
    }
}
