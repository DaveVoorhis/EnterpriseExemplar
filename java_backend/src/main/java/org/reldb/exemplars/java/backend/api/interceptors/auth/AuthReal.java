package org.reldb.exemplars.java.backend.api.interceptors.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;

@Configuration
@Profile("!mockoauth")
public class AuthReal {
    @Bean
    public JwtDecoder getJwtDecoder(@Value("${oauth.issuer}") String issuerLocation) {
        return JwtDecoders.fromIssuerLocation(issuerLocation);
    }
}
