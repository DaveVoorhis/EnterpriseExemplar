package org.reldb.exemplars.java.backend.exception.custom;

import static org.reldb.exemplars.java.backend.exception.ErrorCodes.ERR_PermissionNotFound;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class PermissionNotFoundException extends CustomExceptionBase {
    public PermissionNotFoundException(String permissionName) {
        super(HttpStatus.NOT_FOUND, ERR_PermissionNotFound, "Permission %s does not exist.".formatted(permissionName),
                Map.of("permissionName", permissionName));
    }
}
