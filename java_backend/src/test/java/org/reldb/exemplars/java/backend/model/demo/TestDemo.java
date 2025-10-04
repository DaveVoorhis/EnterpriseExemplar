package org.reldb.exemplars.java.backend.model.demo;

import org.junit.jupiter.api.Test;
import org.reldb.exemplars.java.backend.model.ModelTestBase;

import static org.assertj.core.api.Assertions.assertThat;

public class TestDemo extends ModelTestBase {

    @Test
    void hasSettersAndGetters() {
        final var demo = new Demo();

        final var name = "Test Name";
        final var address = "123 Test Street";

        demo.setName(name);
        demo.setAddress(address);

        assertThat(demo.getName()).isEqualTo(name);
        assertThat(demo.getAddress()).isEqualTo(address);
    }

    @Test
    void nameColumnIsNotNullable() throws Exception {
        assertFieldIsNotNullable(Demo.class, "name");
    }

    @Test
    void addressColumnIsNullable() throws Exception {
        assertFieldIsNullable(Demo.class, "address");
    }

    @Test
    void idIsAnnotatedWithIdAndGeneratedValue() throws Exception {
        assertFieldIsGeneratedSequenceId(Demo.class, "demoId");
    }
}
