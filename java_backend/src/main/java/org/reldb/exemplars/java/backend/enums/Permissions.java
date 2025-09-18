package org.reldb.exemplars.java.backend.enums;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Permissions {
    ADMIN("User administration", Categories.USER_ADMIN),
    FIND_USER("Find user", Categories.USER_ADMIN),
    CHECK_PERMISSION("Verify user has specified permission", Categories.USER_ADMIN),
    GET_ALL_DEMOS("Get all demos", Categories.DEMO_VIEW),
    GET_DEMO("Get a demo", Categories.DEMO_VIEW),
    ADD_DEMO("Add a demo", Categories.DEMO_UPDATE),
    UPDATE_DEMO("Update a demo", Categories.DEMO_UPDATE),
    DELETE_DEMO("Delete a demo", Categories.DEMO_UPDATE);

    private static class Categories {
        private static final String USER_ADMIN = "User Admin";
        private static final String DEMO_VIEW = "Demo View";
        private static final String DEMO_UPDATE = "Demo Update";
    }

    public static List<String> names() {
        return Arrays.stream(values())
                .map(Enum::name)
                .toList();
    }

    private final String description;

    private final String category;
}
