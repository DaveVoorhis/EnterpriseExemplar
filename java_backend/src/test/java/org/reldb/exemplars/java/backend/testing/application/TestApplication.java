package org.reldb.exemplars.java.backend.testing.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.reldb.exemplars.java.backend.ApplicationTestBase;
import org.reldb.exemplars.java.backend.api.UsersApi;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class TestApplication extends ApplicationTestBase {
    private final UsersApi controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }
}
