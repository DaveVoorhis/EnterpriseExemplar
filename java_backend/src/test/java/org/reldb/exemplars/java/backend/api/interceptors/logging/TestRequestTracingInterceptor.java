package org.reldb.exemplars.java.backend.api.interceptors.logging;

import static org.reldb.exemplars.java.backend.api.interceptors.logging.MDCUtils.REQUEST_ID_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Level;
import org.reldb.exemplars.java.backend.exception.custom.InvalidRequestIdException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

@ExtendWith(MockitoExtension.class)
class TestRequestTracingInterceptor {

    private static final String EXPECTED_REQUEST_ID_V4 = "af3bc86d-1d69-429b-b533-69fb06638e4c";
    private static final String EXPECTED_REQUEST_ID_V7 = "018fa134-1f44-7d20-90a2-638e08f02520";

    private final LogAppenderForTests testLogAppender = LogAppenderForTests.initialize();
    private final RequestLoggingInterceptor interceptor = new RequestLoggingInterceptor();

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Object handler;

    @AfterEach
    void afterEach() {
        MDC.remove(REQUEST_ID_KEY);
    }

    @ParameterizedTest
    @ValueSource(strings = {EXPECTED_REQUEST_ID_V4, EXPECTED_REQUEST_ID_V7})
    void storesRequestIdHeaderValueInMDCForLogging(final String requestId) {

        when(request.getHeader(REQUEST_ID_KEY)).thenReturn(requestId);

        interceptor.preHandle(request, response, handler);

        assertThat(MDC.get(REQUEST_ID_KEY)).isEqualTo(requestId);
        verify(request).setAttribute(REQUEST_ID_KEY, requestId);
        verify(response).addHeader(REQUEST_ID_KEY, requestId);
    }

    @Test
    void storesGeneratedRequestIdWhenHeaderNotPresent() {
        interceptor.preHandle(request, response, handler);

        final var generatedUuid = MDC.get(REQUEST_ID_KEY);
        assertThat(generatedUuid).isNotBlank();
        assertThat(UUID.fromString(generatedUuid)).isNotNull();
        verify(request).setAttribute(REQUEST_ID_KEY, generatedUuid);
        verify(response).addHeader(REQUEST_ID_KEY, generatedUuid);
    }

    @Test
    void logsAWarningWhenHeaderNotPresent() {
        interceptor.preHandle(request, response, handler);

        assertThat(testLogAppender.loggedMessages(Level.WARN)).anyMatch(
                s -> s.contains("MDC header not provided so creating MDC ID."));
    }

    @ParameterizedTest
    @CsvSource({"POST,/examples", "GET,/some/resource"})
    void logsRequestReceivedForEndpoint(final String method, final String path) {
        givenRequest(method, path);
        final var requestId = UUID.randomUUID().toString();
        when(request.getHeader(REQUEST_ID_KEY)).thenReturn(requestId);

        interceptor.preHandle(request, response, handler);

        final String expectedLog = String.format("%s: Request received for %s %s", requestId, method, path);
        assertThat(testLogAppender.loggedMessages(Level.DEBUG)).contains(expectedLog);
    }

    @ParameterizedTest
    @CsvSource({"POST,/examples", "GET,/some/resource"})
    void logsRequestCompleteForEndpoint(final String method, final String path) {
        givenRequest(method, path);

        interceptor.afterCompletion(request, response, handler, null);

        final var expectedLog = String.format("Request complete for %s %s", method, path);
        assertThat(testLogAppender.loggedMessages(Level.DEBUG)).contains(expectedLog);
        assertThat(MDC.get(REQUEST_ID_KEY)).isNull();
    }

    @Test
    void throwsExceptionIfInvalidRequestId() {
        when(request.getHeader(REQUEST_ID_KEY)).thenReturn("badRequestId");

        final var thrown = catchThrowable(() -> interceptor.preHandle(request, response, handler));

        final var generatedUuid = MDC.get(REQUEST_ID_KEY);
        assertThat(generatedUuid).isNotBlank();
        assertThat(UUID.fromString(generatedUuid)).isNotNull();
        assertThat(testLogAppender.loggedMessages(Level.WARN)).anyMatch(
                s -> s.contains("MDC header invalid so creating MDC ID."));

        verify(response).addHeader(REQUEST_ID_KEY, generatedUuid);
        assertThat(thrown)
                .isInstanceOf(InvalidRequestIdException.class)
                .hasMessage("Request Id invalid");
    }

    private void givenRequest(final String method, final String path) {
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(path);
    }
}
