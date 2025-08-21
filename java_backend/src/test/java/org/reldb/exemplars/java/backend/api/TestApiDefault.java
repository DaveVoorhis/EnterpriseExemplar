package org.reldb.exemplars.java.backend.api;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.MissingRequestValueException;

class TestApiDefault {
    private final ApiDefault apiDefault = new ApiDefault();

    @Test
    void verifyApiDefaultThrowsException() {
        assertThrows(MissingRequestValueException.class, apiDefault::handleMissingId);
    }
}
