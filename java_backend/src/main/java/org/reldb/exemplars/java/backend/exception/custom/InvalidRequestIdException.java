package org.reldb.exemplars.java.backend.exception.custom;

import static org.reldb.exemplars.java.backend.exception.ErrorCodes.ERR_InvalidRequestId;

import org.springframework.http.HttpStatus;

public class InvalidRequestIdException extends CustomExceptionBase {
    public InvalidRequestIdException() {
        super(HttpStatus.BAD_REQUEST, ERR_InvalidRequestId, "Request Id invalid");
    }
}
