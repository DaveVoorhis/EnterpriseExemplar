package org.reldb.exemplars.java.backend.exception.custom;

import static org.reldb.exemplars.java.backend.exception.ErrorCodes.ERR_SomethingUnusualHappened;

import org.slf4j.event.Level;

public class SomethingUnusualException extends CustomExceptionBase {
    public SomethingUnusualException() {
        super(CustomHttpStatusCode.SOMETHING_UNUSUAL, ERR_SomethingUnusualHappened, "Unusual exception");
        setLogLevel(Level.ERROR);
    }
}
