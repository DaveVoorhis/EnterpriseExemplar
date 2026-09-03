package org.reldb.exemplars.java.backend.testing.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.reldb.exemplars.java.backend.api.ApiTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestRestTemplate
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
