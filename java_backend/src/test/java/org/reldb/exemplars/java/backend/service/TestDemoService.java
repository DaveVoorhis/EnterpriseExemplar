package org.reldb.exemplars.java.backend.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.reldb.exemplars.java.backend.ApplicationTestBase;
import org.reldb.exemplars.java.backend.exception.custom.DemoNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TestDemoService extends ApplicationTestBase {
    private static final long UNKNOWN_DEMO_ID = 999;

    @Autowired
    private DemoService demoService;

    @Test
    void verifyGetAllDemos() {
        final var demos = demoService.getAllDemos();

        assertThat(demos).size().isEqualTo(2);
    }

    @Test
    void canAddUpdateAndRemoveDemo() {
        final var beforeDemos = demoService.getAllDemos();
        assertThat(beforeDemos).size().isEqualTo(2);

        final var newdemo = demoService.addDemo("Dave", "Here");

        final var demosAfterAdd = demoService.getAllDemos();
        assertThat(demosAfterAdd).hasSize(beforeDemos.size() + 1);

        final var updatedName = "Bob";
        final var updatedAddress = "There";

        demoService.updateDemo(newdemo.getDemoId(), updatedName, updatedAddress);

        final var demoAfterUpdate = demoService.getDemo(newdemo.getDemoId());

        assertThat(demoAfterUpdate.getDemoId()).isEqualTo(newdemo.getDemoId());
        assertThat(demoAfterUpdate.getName()).isEqualTo(updatedName);
        assertThat(demoAfterUpdate.getAddress()).isEqualTo(updatedAddress);

        demoService.deleteDemo(newdemo.getDemoId());

        final var demosAfterDelete = demoService.getAllDemos();
        assertThat(demosAfterDelete).hasSameSizeAs(beforeDemos);
    }

    @Test
    void attemptToRetrieveUnknownDemoThrowsException() {
        assertThrows(DemoNotFoundException.class, () -> demoService.getDemo(UNKNOWN_DEMO_ID));
    }

    @Test
    void attemptToUpdateUnknownDemoThrowsException() {
        assertThrows(DemoNotFoundException.class,
                () -> demoService.updateDemo(UNKNOWN_DEMO_ID, "test", "test"));
    }

    @Test
    void attemptToDeleteUnknownDemoThrowsException() {
        assertThrows(DemoNotFoundException.class, () -> demoService.deleteDemo(UNKNOWN_DEMO_ID));
    }
}
