package org.reldb.exemplars.java.backend.exception.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.reldb.exemplars.java.backend.exception.ErrorCodes;
import org.reldb.exemplars.java.backend.exception.ErrorResponse;
import org.reldb.exemplars.java.backend.exception.custom.CustomExceptionBase;
import org.reldb.exemplars.java.backend.exception.custom.CustomHttpStatusCode;
import org.reldb.exemplars.java.backend.exception.custom.InvalidRequestIdException;
import org.reldb.exemplars.java.backend.exception.custom.SomethingUnusualException;
import org.reldb.exemplars.java.backend.exception.custom.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class TestCustomExceptionController {
    private final CustomExceptionController customExceptionController = new CustomExceptionController();

    @Test
    void invalidRequestIdExceptionShouldReturnErrorResponseWithBadRequestError() {
        final var text = "Request Id invalid";
        validateException(new InvalidRequestIdException(), BAD_REQUEST,
                ErrorResponse.custom(ErrorCodes.ERR_InvalidRequestId, text));
    }

    @Test
    void userNotFoundExceptionShouldReturnErrorResponseWithNotFoundError() {
        final var text = "User not found";
        validateException(new UserNotFoundException(), NOT_FOUND,
                ErrorResponse.custom(ErrorCodes.ERR_UserNotFound, text));
    }

    @Test
    void somethingFailedExceptionShouldReturnCustomErrorResponse() {
        final var text = "Unusual exception";
        validateException(new SomethingUnusualException(), CustomHttpStatusCode.SOMETHING_UNUSUAL,
                ErrorResponse.custom(ErrorCodes.ERR_SomethingUnusualHappened, text));
    }

    private void validateException(CustomExceptionBase ex, HttpStatus httpStatus, ErrorResponse expectedErrorResponse) {
        final var errorResponseEntity = customExceptionController
                .handleCustomException(ex);
        final var actualErrorResponse = errorResponseEntity.getBody();

        assertThat(actualErrorResponse).isNotNull();
        assertThat(actualErrorResponse).isEqualTo(expectedErrorResponse);
    }

    private void validateException(CustomExceptionBase ex,
                                   CustomHttpStatusCode httpStatus,
                                   ErrorResponse expectedErrorResponse) {
        final var errorResponseEntity = customExceptionController
                .handleCustomException(ex);
        final var actualErrorResponse = errorResponseEntity.getBody();

        assertThat(actualErrorResponse).isNotNull();
        assertThat(actualErrorResponse).isEqualTo(expectedErrorResponse);
        assertThat(errorResponseEntity.getStatusCode().value()).isEqualTo(httpStatus.getValue());
    }
}