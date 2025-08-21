package org.reldb.exemplars.java.backend.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.reldb.exemplars.java.backend.enums.Permissions;
import org.reldb.exemplars.java.backend.exception.custom.UserLacksPermissionException;
import org.junit.jupiter.api.Test;

public class TestUserLacksPermissionException {
    @Test
    void verifyCorrectMessage() {
        var exception = new UserLacksPermissionException(Permissions.CHECK_PERMISSION);
        assertEquals(
                "User lacks permission CHECK_PERMISSION: \"Verify user has specified permission\"",
                exception.getMessage());
    }
}
