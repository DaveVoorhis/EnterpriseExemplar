package org.reldb.exemplars.java.backend.model.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.reldb.exemplars.java.backend.model.ModelTestBase;

import static org.assertj.core.api.Assertions.assertThat;

public class TestRole extends ModelTestBase {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void hasSettersAndGetters(boolean active) {
        final var role = new Role();

        final var name = "test";
        final var description = "test role";

        role.setName(name);
        role.setDescription(description);
        role.setActive(active);

        assertThat(role.getName()).isEqualTo(name);
        assertThat(role.getDescription()).isEqualTo(description);
        assertThat(role.isActive()).isEqualTo(active);
    }

    @Test
    void nameColumnIsNotNullable() throws Exception {
        assertFieldIsNotNullable(Role.class, "name");
    }

    @Test
    void descriptionColumnIsNotNullable() throws Exception {
        assertFieldIsNotNullable(Role.class, "description");
    }

    @Test
    void idIsAnnotatedWithIdAndGeneratedValue() throws Exception {
        assertFieldIsGeneratedSequenceId(Role.class, "roleId");
    }
}
