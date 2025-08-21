package org.reldb.exemplars.java.backend.api.interceptors.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.reldb.exemplars.java.backend.api.ApiTestBase;
import org.reldb.exemplars.java.backend.api.model.UserOut;
import org.reldb.exemplars.java.backend.api.service.UserServiceAdapter;
import java.time.Instant;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class TestUserInterceptor extends ApiTestBase {
    public static final String ENABLED_NOT_REQUIRED_URL = "/users/current";

    @MockitoBean
    UserServiceAdapter userService;

    @ParameterizedTest
    @MethodSource("canCallEndpointData")
    @DisplayName("A user doesn't need to be enabled to access an endpoint where enabled isn't required.")
    void canCallEndpoint(boolean enabled, HttpStatus expectedResponse) {
        when(userService.login(anyString()))
                .thenReturn(new UserOut(1L, "blah@blah.com", enabled, Instant.now()));

        final var url = getBaseUrl() + ENABLED_NOT_REQUIRED_URL;

        final var headers = getHeaders();

        final var response = testRestTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(expectedResponse);
    }

    private static Stream<Arguments> canCallEndpointData() {
        return Stream.of(
                Arguments.of(true, HttpStatus.OK),
                Arguments.of(false, HttpStatus.OK));
    }
}
