package org.reldb.exemplars.java.backend.exception.custom;

import org.reldb.exemplars.java.backend.exception.ErrorCodes;
import org.reldb.exemplars.java.backend.exception.ErrorResponse;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class CustomExceptionBase extends RuntimeException {
    private final HttpStatusCode httpStatusCode;
    private final CustomHttpStatusCode customHttpStatusCode;

    @Getter
    private final ErrorResponse response;

    @Getter
    @Setter
    private Level logLevel = Level.WARN;

    public CustomExceptionBase(HttpStatus httpStatusCode, ErrorCodes errorCode, String message,
            Map<String, Object> details) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.customHttpStatusCode = null;
        this.response = ErrorResponse.custom(errorCode, message, details);
    }

    public CustomExceptionBase(HttpStatus httpStatusCode, ErrorCodes errorCode, String message) {
        this(httpStatusCode, errorCode, message, Map.of());
    }

    public CustomExceptionBase(CustomHttpStatusCode customHttpStatusCode, ErrorCodes errorCode, String message,
            Map<String, Object> details) {
        super(message);
        this.httpStatusCode = null;
        this.customHttpStatusCode = customHttpStatusCode;
        this.response = ErrorResponse.custom(errorCode, message, details);
    }

    public CustomExceptionBase(CustomHttpStatusCode customHttpStatusCode, ErrorCodes errorCode, String message) {
        this(customHttpStatusCode, errorCode, message, Map.of());
    }

    public HttpStatusCode getStatusCode() {
        return httpStatusCode != null
                ? httpStatusCode
                : HttpStatusCode.valueOf(customHttpStatusCode.getValue());
    }
}
