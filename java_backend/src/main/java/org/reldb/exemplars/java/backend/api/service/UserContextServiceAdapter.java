package org.reldb.exemplars.java.backend.api.service;

import org.reldb.exemplars.java.backend.service.UserContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserContextServiceAdapter {
    @Autowired
    private UserContextService userContextService;

    public String getUsername() {
        return userContextService.getUsername();
    }
}
