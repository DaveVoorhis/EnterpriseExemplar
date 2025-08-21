package org.reldb.exemplars.java.backend.api.interceptors.logging;

import org.reldb.exemplars.java.backend.api.interceptors.RequestHandlerBase;
import org.reldb.exemplars.java.backend.exception.custom.InvalidRequestIdException;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Intercept service endpoint calls and log header X-Request-Id value so we can correlate backend
 * calls with frontend activity in the logs. See
 * <a href="https://www.baeldung.com/mdc-in-log4j-2-logback">MDC</a>.
 *
 * @see <a href="file:src/main/resources/logback-spring.xml">logback-spring.xml</a> to configure the
 *      layout of the logs.
 */
@Slf4j
public class RequestLoggingInterceptor extends RequestHandlerBase implements HandlerInterceptor {
    public static final String UUID_MATCHER_PATTERN = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) {
        final var endpoint = endpointDescriptor(request);
        final var requestId = request.getHeader(MDCUtils.REQUEST_ID_KEY);
        if (requestId == null) {
            addRequestId(response, UUID.randomUUID().toString());
            log.warn("{} MDC header not provided so creating MDC ID.", MDCUtils.REQUEST_ID_KEY);
        } else if (isRequestIdInvalid(requestId)) {
            addRequestId(response, UUID.randomUUID().toString());
            log.warn("{} MDC header invalid so creating MDC ID.", MDCUtils.REQUEST_ID_KEY);
            throw new InvalidRequestIdException();
        } else {
            addRequestId(response, requestId);
        }
        request.setAttribute(MDCUtils.REQUEST_ID_KEY, MDCUtils.getMDC(MDCUtils.REQUEST_ID_KEY));
        log.debug("Request received for {}", endpoint);
        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request,
                                @NotNull HttpServletResponse response,
                                @NotNull Object handler,
                                @Nullable Exception ex) {
        final var endpoint = endpointDescriptor(request);
        log.debug("Request complete for {}", endpoint);
        MDCUtils.clearMDC(MDCUtils.REQUEST_ID_KEY);
    }

    private void addRequestId(@NotNull HttpServletResponse response, @NotNull String uuid) {
        MDCUtils.updateMDC(MDCUtils.REQUEST_ID_KEY, uuid);
        addRequestIdToResponse(response);
    }

    private boolean isRequestIdInvalid(@NotNull String requestId) {
        return !requestId.matches(UUID_MATCHER_PATTERN);
    }

    private void addRequestIdToResponse(HttpServletResponse response) {
        response.addHeader(MDCUtils.REQUEST_ID_KEY, MDCUtils.getMDC(MDCUtils.REQUEST_ID_KEY));
    }
}
