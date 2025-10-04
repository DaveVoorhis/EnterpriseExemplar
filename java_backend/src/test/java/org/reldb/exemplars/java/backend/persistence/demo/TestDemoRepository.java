package org.reldb.exemplars.java.backend.persistence.demo;

import org.junit.jupiter.api.Test;
import org.reldb.exemplars.java.backend.ApplicationTestBase;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class TestDemoRepository extends ApplicationTestBase {
    @Autowired
    private DemoRepository repository;

    @Test
    void canFindAllDemos() {
        final var demos = repository.findAll();

        assertThat(demos).hasSize(2);

        final var iterator = demos.iterator();

        final var demo0 = iterator.next();
        assertThat(demo0.getDemoId()).isEqualTo(1);
        assertThat(demo0.getName()).isEqualTo("demo1");
        assertThat(demo0.getAddress()).isEqualTo("here");

        final var demo1 = iterator.next();
        assertThat(demo1.getDemoId()).isEqualTo(2);
        assertThat(demo1.getName()).isEqualTo("demo2");
        assertThat(demo1.getAddress()).isEqualTo("there");
    }
}
