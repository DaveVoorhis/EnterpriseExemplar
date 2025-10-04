package org.reldb.exemplars.java.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelTestBase {
    protected void assertFieldIsGeneratedSequenceId(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        final var field = clazz.getDeclaredField(fieldName);
        assertThat(field.isAnnotationPresent(Id.class)).isTrue();
        assertThat(field.isAnnotationPresent(GeneratedValue.class)).isTrue();
        assertThat(field.isAnnotationPresent(SequenceGenerator.class)).isTrue();
    }

    protected void assertFieldIsNotNullable(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        assertFieldNullability(clazz, fieldName, false);
    }

    protected void assertFieldIsNullable(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        assertFieldNullability(clazz, fieldName, true);
    }

    private void assertFieldNullability(Class<?> clazz, String fieldName, boolean nullable) throws NoSuchFieldException {
        final var field = clazz.getDeclaredField(fieldName);
        final var columnAnnotation = field.getAnnotation(Column.class);
        assertThat(columnAnnotation).isNotNull();
        assertThat(columnAnnotation.nullable()).isEqualTo(nullable);
    }
}
