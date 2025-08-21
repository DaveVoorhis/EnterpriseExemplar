package org.reldb.exemplars.java.backend.exception.custom;

import static org.reldb.exemplars.java.backend.exception.ErrorCodes.ERR_RoleNotFound;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends CustomExceptionBase {
    public RoleNotFoundException(long roleId) {
        super(HttpStatus.NOT_FOUND, ERR_RoleNotFound, "Role ID %s not found.".formatted(roleId),
                Map.of("roleId", roleId));
    }
}
