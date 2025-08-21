package org.reldb.exemplars.java.backend.exception.custom;

import static org.reldb.exemplars.java.backend.exception.ErrorCodes.ERR_UserNotFound;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomExceptionBase {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, ERR_UserNotFound, "User not found");
    }
}
