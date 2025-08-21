package org.reldb.exemplars.java.backend.exception.custom;

import static org.reldb.exemplars.java.backend.exception.ErrorCodes.ERR_UserCredentialsInvalid;

import org.springframework.http.HttpStatus;

public class UserCredentialsInvalidException extends CustomExceptionBase {
    public UserCredentialsInvalidException() {
        super(HttpStatus.UNAUTHORIZED, ERR_UserCredentialsInvalid, "Invalid credentials.");
    }
}
