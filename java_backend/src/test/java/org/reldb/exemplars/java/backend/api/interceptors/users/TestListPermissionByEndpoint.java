package org.reldb.exemplars.java.backend.api.interceptors.users;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reldb.exemplars.java.backend.api.ApiTestBase;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class TestListPermissionByEndpoint extends ApiTestBase {
    private final PermissionLister permissionLister;

    @Test
    @DisplayName("List of API endpoints and required permissions.")
    void listPermissionsByEndpoint() {
        final var title = "API Endpoint Permission Required List -- blank means any enabled user can access";
        final var margin = "=".repeat(25);
        final var header = margin + " " + title + " " + margin;
        final var footer = "-".repeat(header.length());

        System.out.println(header);
        permissionLister.getPermissions()
                .forEach(System.out::println);
        System.out.println(footer);
    }
}
