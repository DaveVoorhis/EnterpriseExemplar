package org.reldb.exemplars.java.backend.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

class ApiDefault {
    // This hidden endpoint handler is here to nicely handle the case of invalid default path.
    @RequestMapping(value = "/", method = {RequestMethod.GET,
            RequestMethod.PUT, RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
    void handleMissingId() throws MissingRequestValueException {
        // Unreachable - Spring will throw before this is reached.
        throw new MissingRequestValueException("parameter");
    }
}
