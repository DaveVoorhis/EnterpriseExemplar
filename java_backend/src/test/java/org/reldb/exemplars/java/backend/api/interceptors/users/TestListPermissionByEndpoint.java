package org.reldb.exemplars.java.backend.api.interceptors.users;

import org.reldb.exemplars.java.backend.api.ApiTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TestListPermissionByEndpoint extends ApiTestBase {
    @Autowired
    PermissionLister permissionLister;

    @Test
    @DisplayName("List of API endpoints and required permissions.")
    void listPermissionsByEndpoint() {
        final var title = "API Endpoint Permission Required List -- blank means any enabled user can access";
        final var margin = "=".repeat(25);
        final var header = margin + " " + title + " " + margin;
        final var footer = "-".repeat(header.length());

        System.out.println(header);
        permissionLister.getPermissions()
                .forEach(method -> System.out.println(method.toString()));
        System.out.println(footer);
    }
}
