package org.reldb.exemplars.java.backend.exception.custom;

import static org.reldb.exemplars.java.backend.exception.ErrorCodes.ERR_UserNotEnabled;

import org.springframework.http.HttpStatus;

public class UserNotEnabledException extends CustomExceptionBase {
    public UserNotEnabledException() {
        super(HttpStatus.UNAUTHORIZED, ERR_UserNotEnabled, "User not enabled.");
    }
}
