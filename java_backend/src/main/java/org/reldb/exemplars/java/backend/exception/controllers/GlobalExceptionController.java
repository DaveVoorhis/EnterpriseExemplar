package org.reldb.exemplars.java.backend.exception.controllers;

import static org.springframework.http.HttpStatus.*;

import org.reldb.exemplars.java.backend.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.jspecify.annotations.NonNull;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
@AllArgsConstructor
public class GlobalExceptionController {
    @ExceptionHandler(value = {NotImplementedException.class})
    @ResponseStatus(NOT_IMPLEMENTED)
    @ResponseBody
    public ResponseEntity<@NonNull ErrorResponse> handleNotImplementedException(NotImplementedException e) {
        log.error("NotImplementedException: ", e);
        return new ResponseEntity<>(ErrorResponse.unimplemented(), NOT_IMPLEMENTED);
    }

    @ExceptionHandler(value = {NoResourceFoundException.class})
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    public ResponseEntity<@NonNull ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        log.error("NoResourceFoundException: ", e);
        return new ResponseEntity<>(ErrorResponse.resourceNotFound(e.getMessage()), NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<@NonNull ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException: ", e);
        return new ResponseEntity<>(ErrorResponse.badRequest(e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeException.class
    })
    public ResponseEntity<@NonNull ErrorResponse> handleOtherBadRequest(Exception e) {
        log.error("OtherBadRequestException: ", e);
        final var errorResponse = ErrorResponse.badRequest(e.getMessage());
        /*
         * Create the ResponseEntity explicitly to set the contentType explicitly to
         * Application/json. Without it, Spring can -- depending on nuances of the api/controller
         * config, particularly if things are misconfigured -- attempt to convert the response to
         * the requested media type, and probably fail in the process thereby producing a different
         * exception and returning a 500, thus hiding the true cause from the caller.
         */
        if (e instanceof org.springframework.web.ErrorResponse e2) {
            return ResponseEntity
                    .status(e2.getStatusCode())
                    .headers(e2.getHeaders())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);
        }
        // Return null to fall through to global handler.
        return null;
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<@NonNull ErrorResponse> handleUnexpectedException(Exception e) {
        log.error("InternalServerError: ", e);
        return new ResponseEntity<>(ErrorResponse.serverError(), INTERNAL_SERVER_ERROR);
    }
}
