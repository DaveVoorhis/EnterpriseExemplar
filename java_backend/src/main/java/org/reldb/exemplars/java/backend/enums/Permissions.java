package org.reldb.exemplars.java.backend.enums;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Permissions {
    ADMIN("User administration", Categories.ADMIN),
    FIND_USER("Find user", Categories.VIEW),
    CHECK_PERMISSION("Verify user has specified permission", Categories.VIEW),
    GET_ALL_DEMOS("Get all demos", Categories.VIEW),
    ADD_DEMO("Add a demo", Categories.UPDATE),
    UPDATE_DEMO("Update a demo", Categories.UPDATE),
    DELETE_DEMO("Delete a demo", Categories.UPDATE),
    GET_DEMO("Get a demo", Categories.VIEW);

    private static class Categories {
        private static final String ADMIN = "Admin";
        private static final String VIEW = "View";
        private static final String UPDATE = "Update";
    }

    public static List<String> names() {
        return Arrays.stream(values())
                .map(Enum::name)
                .toList();
    }

    private final String description;

    private final String category;
}
