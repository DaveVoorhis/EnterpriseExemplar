package org.reldb.exemplars.java.backend.service;

import org.reldb.exemplars.java.backend.exception.custom.UserCredentialsInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserContextService {
    private static final String USERNAME = "preferred_username";

    @Autowired
    private SecurityContextProvider securityContextProvider;

    public String getUsername() {
        final var auth = securityContextProvider.getAuthentication();
        final var jsonWebToken = checkCredentials(auth);
        return (String) jsonWebToken.getClaims().get(USERNAME);
    }

    private Jwt checkCredentials(Authentication auth) {
        if (auth != null && auth.getCredentials() instanceof Jwt jwt) {
            return jwt;
        }
        log.warn("Invalid security credentials.");
        throw new UserCredentialsInvalidException();
    }
}
