package org.reldb.exemplars.java.backend.api.model;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record UserOut(
        long userId,
        @NotNull String email,
        boolean enabled,
        @NotNull Instant lastLogin) {
}