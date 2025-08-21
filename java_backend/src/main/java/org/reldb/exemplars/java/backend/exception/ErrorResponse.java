package org.reldb.exemplars.java.backend.exception;

import static org.reldb.exemplars.java.backend.exception.ErrorCodes.*;

import java.util.Map;
import org.jspecify.annotations.NonNull;

public record ErrorResponse(Map<String, Object> response) {

    private ErrorResponse(ErrorCodes errorCode, String message, Map<String, Object> details) {
        this(Map.of(
                "code", errorCode,
                "details", details,
                "message", message));
    }

    public static @NonNull ErrorResponse custom(ErrorCodes errorCode, String message, Map<String, Object> details) {
        return new ErrorResponse(errorCode, message, details);
    }

    public static @NonNull ErrorResponse custom(ErrorCodes errorCode, String message) {
        return custom(errorCode, message, Map.of());
    }

    public static @NonNull ErrorResponse badRequest(String message) {
        return custom(ERR_BadRequest, "Bad request. Details: %s".formatted(message), Map.of("text", message));
    }

    public static @NonNull ErrorResponse unauthorized() {
        return custom(ERR_Unauthorized, "Unauthorized");
    }

    public static @NonNull ErrorResponse resourceNotFound(String message) {
        return custom(ERR_NotFound, "Not found. Details: %s".formatted(message), Map.of("text", message));
    }

    public static @NonNull ErrorResponse serverError() {
        return custom(ERR_UnexpectedServerError, "Unexpected server error");
    }

    public static @NonNull ErrorResponse unimplemented() {
        return custom(ERR_NotImplemented, "Not implemented");
    }
}
