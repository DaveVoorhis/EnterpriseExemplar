package org.reldb.exemplars.java.backend.api.interceptors.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.reldb.exemplars.java.backend.api.ApiTestBase;
import org.reldb.exemplars.java.backend.service.SecurityContextProvider;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class TestUserInterceptorUnauthorised extends ApiTestBase {
    public static final String VALID_URL = "/users/%s";

    @MockitoBean
    SecurityContextProvider securityContextProvider;

    @Test
    void defaultAccessIsUnauthorised() {
        when(securityContextProvider.getAuthentication())
                .thenReturn(null);

        final var url = getBaseUrl() + VALID_URL.formatted(4);

        final var headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "bearer dummy");

        final var response = testRestTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
