package org.reldb.exemplars.java.backend.exception.custom;

import static org.reldb.exemplars.java.backend.exception.ErrorCodes.ERR_UserLacksPermission;

import org.reldb.exemplars.java.backend.enums.Permissions;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class UserLacksPermissionException extends CustomExceptionBase {
    public UserLacksPermissionException(Permissions permission) {
        super(HttpStatus.FORBIDDEN,
                ERR_UserLacksPermission,
                "User lacks permission %s: \"%s\"".formatted(permission, permission.getDescription()),
                Map.of(
                        "permission", permission,
                        "permissionDescription", permission.getDescription(),
                        "permissionCategory", permission.getCategory()));
    }
}
