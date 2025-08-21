package org.reldb.exemplars.java.backend.api.interceptors;

import jakarta.servlet.http.HttpServletRequest;

public class RequestHandlerBase {
    protected String endpointDescriptor(HttpServletRequest request) {
        return String.format("%s %s", request.getMethod(), request.getRequestURI());
    }
}
