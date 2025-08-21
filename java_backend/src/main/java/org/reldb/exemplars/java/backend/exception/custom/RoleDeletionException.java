package org.reldb.exemplars.java.backend.exception.custom;

import static org.reldb.exemplars.java.backend.exception.ErrorCodes.ERR_RoleDeletionDisallowed;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class RoleDeletionException extends CustomExceptionBase {
    public RoleDeletionException(long roleId) {
        super(HttpStatus.BAD_REQUEST, ERR_RoleDeletionDisallowed,
                "Special role ID %s may not be deleted.".formatted(roleId),
                Map.of("roleId", roleId));
    }
}
