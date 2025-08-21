package org.reldb.exemplars.java.backend.testing.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.reldb.exemplars.java.backend.ApplicationTestBase;
import org.reldb.exemplars.java.backend.api.UsersApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TestApplication extends ApplicationTestBase {

    @Autowired
    private UsersApi controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }
}
