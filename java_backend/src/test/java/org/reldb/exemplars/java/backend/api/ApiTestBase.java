package org.reldb.exemplars.java.backend.api;

import org.reldb.exemplars.java.backend.Application;
import org.reldb.exemplars.java.backend.ApplicationTestBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.test.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"server.port=0"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = Application.class)
public class ApiTestBase extends ApplicationTestBase {

    protected static final String X_REQUEST_ID = "X-Request-Id";

    @Value("${local.server.port}")
    private int servicePort;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    protected String getBaseUrl() {
        return "http://localhost:%d/api".formatted(servicePort);
    }

    protected HttpHeaders getHeaders() {
        final var headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "bearer dummy");
        return headers;
    }

    protected String getQuery(Map<String, Object> parameterArgs) {
        return parameterArgs.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .map(entry -> urlEncodeUTF8(entry.getKey()) + "=" + urlEncodeUTF8(entry.getValue().toString()))
                .reduce((keyval1, keyval2) -> keyval1 + "&" + keyval2)
                .orElse("");
    }

    protected <T> ResponseEntity<T> get(String url) {
        return get(url, new ParameterizedTypeReference<>() {
        });
    }

    // Use this when Jackson's "best guess" deserialization doesn't work.
    protected <T> ResponseEntity<T> get(String url, ParameterizedTypeReference<T> t) {
        return call(url, HttpMethod.GET, t);
    }

    protected <T> ResponseEntity<T> delete(String url) {
        return delete(url, new ParameterizedTypeReference<>() {
        });
    }

    // Use this when Jackson's "best guess" deserialization doesn't work.
    protected <T> ResponseEntity<T> delete(String url, ParameterizedTypeReference<T> t) {
        return call(url, HttpMethod.DELETE, t);
    }

    protected <T> ResponseEntity<T> post(String url, Object in) throws JsonProcessingException {
        return post(url, new ParameterizedTypeReference<>() {
        }, in);
    }

    // Use this when Jackson's "best guess" deserialization doesn't work.
    protected <T> ResponseEntity<T> post(String url, ParameterizedTypeReference<T> t, Object in)
            throws JsonProcessingException {
        return call(url, HttpMethod.POST, t, in);
    }

    protected <T> ResponseEntity<T> put(String url, Object in) throws JsonProcessingException {
        return put(url, new ParameterizedTypeReference<>() {
        }, in);
    }

    // Use this when Jackson's "best guess" deserialization doesn't work.
    protected <T> ResponseEntity<T> put(String url, ParameterizedTypeReference<T> t, Object in)
            throws JsonProcessingException {
        return call(url, HttpMethod.PUT, t, in);
    }

    private <T> ResponseEntity<T> call(String url, HttpMethod method, ParameterizedTypeReference<T> t) {
        return call(url, method, new HttpEntity<>(getHeaders()), t);
    }

    private <T> ResponseEntity<T> call(String url, HttpMethod method, ParameterizedTypeReference<T> t, Object in)
            throws JsonProcessingException {
        final var headers = getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final var mapper = new ObjectMapper();
        final var requestBody = mapper.writeValueAsString(in);

        return call(url, method, new HttpEntity<>(requestBody, headers), t);
    }

    private <T> ResponseEntity<T> call(String url,
                                       HttpMethod method,
                                       HttpEntity<?> entity,
                                       ParameterizedTypeReference<T> t) {
        return testRestTemplate.exchange(url, method, entity, t);
    }

    private String urlEncodeUTF8(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}
