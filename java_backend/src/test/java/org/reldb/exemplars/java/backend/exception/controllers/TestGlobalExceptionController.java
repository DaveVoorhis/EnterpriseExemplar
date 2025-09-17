package org.reldb.exemplars.java.backend.exception.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.reldb.exemplars.java.backend.exception.ErrorResponse;
import org.apache.commons.lang3.NotImplementedException;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

public class TestGlobalExceptionController {

    private final GlobalExceptionController globalExceptionController = new GlobalExceptionController();

    @Test
    void handleUnexpectedExceptionShouldReturnErrorResponseWithInternalServerError() {
        final var ex = mock(Exception.class);

        final var errorResponseEntity = globalExceptionController
                .handleUnexpectedException(ex);
        final var expectedErrorResponse = ErrorResponse.serverError();

        validateException(errorResponseEntity, expectedErrorResponse);
    }

    @Test
    void handleNoResourceFoundExceptionShouldReturnErrorResponseWithNotFoundError() {
        final var ex = new NoResourceFoundException(HttpMethod.GET, "http://something", "/non/existence");

        final var errorResponseEntity = globalExceptionController
                .handleNoResourceFoundException(ex);
        final var expectedErrorResponse = ErrorResponse.resourceNotFound("No static resource /non/existence for request 'http://something'.");

        validateException(errorResponseEntity, expectedErrorResponse);
    }

    @Test
    void handleNotImplementedExceptionShouldReturnErrorResponseWithNotFoundError() {
        final var ex = new NotImplementedException("Method not implemented.");

        final var errorResponseEntity = globalExceptionController
                .handleNotImplementedException(ex);
        final var expectedErrorResponse = ErrorResponse.unimplemented();

        validateException(errorResponseEntity, expectedErrorResponse);
    }

    @Test
    void handleMethodArgumentTypeMismatchShouldReturnErrorResponseWithBadRequestError() {
        final var ex = new MethodArgumentTypeMismatchException(null, null, "",
                mock(MethodParameter.class), null);

        final var errorResponseEntity = globalExceptionController
                .handleMethodArgumentTypeMismatchException(ex);
        final var expectedErrorResponse = ErrorResponse
                .badRequest("Method parameter '': Failed to convert value of type 'null'");

        validateException(errorResponseEntity, expectedErrorResponse);
    }

    @Test
    void handleWrongMediaTypeValidationException() {
        final var text = "Content-Type 'application/txt;charset=UTF-8' is not supported.";
        final var ex = new HttpMediaTypeNotSupportedException(text);

        final var errorResponseEntity = globalExceptionController
                .handleOtherBadRequest(ex);
        final var expectedErrorResponse = ErrorResponse.badRequest(text);

        validateException(errorResponseEntity, expectedErrorResponse);
    }

    @Test
    void handleBadRequestShouldFallThroughIfExceptionNotConvertedToErrorResponse() {
        final var ex = new RuntimeException("Not usual exception type.");

        final var errorResponseEntity = globalExceptionController.handleOtherBadRequest(ex);

        assertThat(errorResponseEntity).isNull();
    }

    private void validateException(ResponseEntity<@NonNull ErrorResponse> errorResponseEntity,
                                   ErrorResponse expectedErrorResponse) {
        final var actualErrorResponse = errorResponseEntity.getBody();

        assertThat(actualErrorResponse).isNotNull();
        assertThat(actualErrorResponse).isEqualTo(expectedErrorResponse);
    }
}