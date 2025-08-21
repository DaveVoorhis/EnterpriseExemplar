package org.reldb.exemplars.java.backend.exception.custom;

import static org.reldb.exemplars.java.backend.exception.ErrorCodes.ERR_DemoNotFound;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class DemoNotFoundException extends CustomExceptionBase {
    public DemoNotFoundException(long demoId) {
        super(HttpStatus.NOT_FOUND, ERR_DemoNotFound, "Demo ID " + demoId + " not found.",
                Map.of("demoId", demoId));
    }
}
