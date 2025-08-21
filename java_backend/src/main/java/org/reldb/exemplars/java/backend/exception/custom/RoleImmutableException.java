package org.reldb.exemplars.java.backend.exception.custom;

import static org.reldb.exemplars.java.backend.exception.ErrorCodes.ERR_RoleImmutable;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class RoleImmutableException extends CustomExceptionBase {
    public RoleImmutableException(long roleId) {
        super(HttpStatus.BAD_REQUEST, ERR_RoleImmutable,
                "Role ID %s disallows change of permissions.".formatted(roleId),
                Map.of("roleId", roleId));
    }
}
