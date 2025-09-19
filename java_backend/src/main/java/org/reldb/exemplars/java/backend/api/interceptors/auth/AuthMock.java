package org.reldb.exemplars.java.backend.api.interceptors.auth;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.Instant;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
@Profile("noauth")
public class AuthMock {
    @Autowired
    private MockSecureUser secureUser;

    /**
     * This should only be enabled (via the noauth profile) when running locally or in a dev
     * environment, to make sure unit tests can pass without having a real JWT token. The user
     * associated with 'email' must exist and be retrievable from the main database's
     * app_users table and have 'enabled' set to true.
     */
    @Bean
    public JwtDecoder getDummyJwtDecoder() {
        return ((token) -> {
            final var issuedAt = Instant.now().minus(1, SECONDS);
            final var expiresAt = Instant.now().plus(1, HOURS);
            final Map<String, Object> headers = Map.of(
                    "alg", "RS256",
                    "typ", "JWT");
            final Map<String, Object> claims = Map.of(
                    "iss", "mock",
                    "email", secureUser.getUserid(),
                    "name", secureUser.getUsername());
            return new Jwt(token, issuedAt, expiresAt, headers, claims);
        });
    }
}
