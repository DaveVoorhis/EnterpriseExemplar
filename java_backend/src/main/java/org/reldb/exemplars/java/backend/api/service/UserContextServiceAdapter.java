package org.reldb.exemplars.java.backend.api.service;

import lombok.RequiredArgsConstructor;
import org.reldb.exemplars.java.backend.service.UserContextService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserContextServiceAdapter {
    private final UserContextService userContextService;

    public String getUsername() {
        return userContextService.getUsername();
    }
}
