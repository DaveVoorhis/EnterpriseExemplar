package org.reldb.exemplars.java.backend.exception.custom;

import static org.reldb.exemplars.java.backend.exception.ErrorCodes.ERR_RoleInUseNotDeleted;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class RoleInUseMayNotBeDeletedException extends CustomExceptionBase {
    public RoleInUseMayNotBeDeletedException(long roleId) {
        super(HttpStatus.BAD_REQUEST, ERR_RoleInUseNotDeleted,
                "At least one user is assigned role ID %s.".formatted(roleId),
                Map.of("roleId", roleId));
    }
}
