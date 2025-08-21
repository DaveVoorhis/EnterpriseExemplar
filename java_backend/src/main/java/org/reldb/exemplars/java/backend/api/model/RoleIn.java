package org.reldb.exemplars.java.backend.api.model;

import org.jspecify.annotations.NonNull;

public record RoleIn(@NonNull String name, String description, boolean active) {
}