package org.reldb.exemplars.java.backend.api.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reldb.exemplars.java.backend.service.UserContextService;

@ExtendWith(MockitoExtension.class)
class TestUserContextServiceAdapter {

    @Mock
    private UserContextService service;

    @InjectMocks
    private UserContextServiceAdapter adapter;

    @Test
    void verifyGetUsername() {
        adapter.getUsername();

        verify(service, times(1)).getUsername();
    }
}
