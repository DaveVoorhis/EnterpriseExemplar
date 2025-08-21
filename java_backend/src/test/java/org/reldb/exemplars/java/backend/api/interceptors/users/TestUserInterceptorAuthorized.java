package org.reldb.exemplars.java.backend.api.interceptors.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.reldb.exemplars.java.backend.api.ApiTestBase;
import org.reldb.exemplars.java.backend.service.UserContextService;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestUserInterceptorAuthorized extends ApiTestBase {
    public static final String USERS_URL = "/users/%s";
    public static final String GET_ALL_USERS_URL = "/users";
    public static final int VALID_USER_ID = 4;

    @MockitoBean
    UserContextService userContextService;

    @ParameterizedTest
    @MethodSource("canCallEndpointData")
    @DisplayName("A user must be enabled and have permission to access an endpoint.")
    void canCallEnabledRequiredEndpoint(String username, String urlPart, HttpStatus expectedResponse) {
        when(userContextService.getUsername())
                .thenReturn(username);

        final var url = getBaseUrl() + urlPart;

        final var headers = getHeaders();

        final var response = testRestTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(expectedResponse);
    }

    private static Stream<Arguments> canCallEndpointData() {
        return Stream.of(
                Arguments.of("dave.voorhis@reldb.org", USERS_URL.formatted(VALID_USER_ID), HttpStatus.OK),
                Arguments.of("zap.zot@reldb.org", USERS_URL.formatted(VALID_USER_ID), HttpStatus.UNAUTHORIZED),
                Arguments.of("blah.blah@reldb.org", GET_ALL_USERS_URL, HttpStatus.FORBIDDEN));
    }
}
