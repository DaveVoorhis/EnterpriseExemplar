package org.reldb.exemplars.java.backend.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.reldb.exemplars.java.backend.ApplicationTestBase;
import org.reldb.exemplars.java.backend.exception.custom.UserCredentialsInvalidException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class TestUserContextService extends ApplicationTestBase {
    @Autowired
    UserContextService service;
    @MockitoBean
    SecurityContextProvider securityContextProvider;

    @Test
    void verifyExceptionForBogusCredentials() {
        when(securityContextProvider.getAuthentication())
                .thenCallRealMethod();
        assertThrows(UserCredentialsInvalidException.class,
                () -> service.getUsername());
    }

    @Test
    void verifyExceptionForNullAuthentication() {
        when(securityContextProvider.getAuthentication())
                .thenReturn(null);
        assertThrows(UserCredentialsInvalidException.class,
                () -> service.getUsername());
    }

    @Test
    void verifyExceptionForNonJwtAuthentication() {
        when(securityContextProvider.getAuthentication())
                .thenReturn(new AnonymousAuthenticationToken("blah", "blah",
                        List.of((GrantedAuthority) () -> "blah")));
        assertThrows(UserCredentialsInvalidException.class,
                () -> service.getUsername());
    }

    @Test
    void verifyValidJwtAuthentication() {
        final var username = "dave";
        final var jwt = new Jwt("blah",
                Instant.now(), Instant.now().plus(10, ChronoUnit.SECONDS),
                Map.of("blah", "blah"),
                Map.of("email", username));
        final var token = new JwtAuthenticationToken(jwt);
        when(securityContextProvider.getAuthentication())
                .thenReturn(token);
        assertThat(service.getUsername()).isEqualTo(username);
    }
}
