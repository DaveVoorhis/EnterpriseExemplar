package org.reldb.exemplars.java.backend.api.interceptors.security;

import org.reldb.exemplars.java.backend.api.interceptors.RequestHandlerBase;
import org.reldb.exemplars.java.backend.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthFail extends RequestHandlerBase implements AuthenticationEntryPoint {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {
        log.error("Unauthorized attempt to access {}", request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().print(mapper.writeValueAsString(
                ErrorResponse.unauthorized()));
    }
}
