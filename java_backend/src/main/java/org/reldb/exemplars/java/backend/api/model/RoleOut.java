package org.reldb.exemplars.java.backend.api.model;

import org.jspecify.annotations.NonNull;

public record RoleOut(long roleId, @NonNull String name, @NonNull String description, boolean active) {
}
