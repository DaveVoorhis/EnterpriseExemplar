package org.reldb.exemplars.java.backend.testing.application;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.RequiredArgsConstructor;
import org.reldb.exemplars.java.backend.api.ApiTestBase;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.test.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TestHealth extends ApiTestBase {

    @Value("${local.management.port}")
    private int managementPort;

    private final TestRestTemplate testRestTemplate;

    @Test
    public void shouldReturn200FromHealthCheckEndpoint() {
        final var url = String.format("http://localhost:%d/actuator/health", managementPort);
        final var response = testRestTemplate.getForEntity(url, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
