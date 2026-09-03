package org.reldb.exemplars.java.backend.cron;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.reldb.exemplars.java.backend.ApplicationTestBase;
import org.reldb.exemplars.java.backend.service.PeriodicBackgroundTaskService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@TestPropertySource(properties = {
        "periodic-background-task.schedule=* * * * * *"})
@DirtiesContext
public class TestCronTasks extends ApplicationTestBase {

    @MockitoBean
    private PeriodicBackgroundTaskService periodicBackgroundTaskService;

    @Test
    void verifyCronTasksRun() {
        waitForCronTasksToRun();

        verify(periodicBackgroundTaskService, atLeast(2)).doSomething();
    }

    private void waitForCronTasksToRun() {
        try {
            Thread.sleep(Duration.ofSeconds(5));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
