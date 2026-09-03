package org.reldb.exemplars.java.backend.cron;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reldb.exemplars.java.backend.service.PeriodicBackgroundTaskService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PeriodicBackgroundTask {
    private final PeriodicBackgroundTaskService periodicBackgroundTaskService;

    @Scheduled(cron = "${periodic-background-task.schedule}")
    public void removeExpiredSuspensions() {
        log.debug("Scheduled periodic background task processing.");
        periodicBackgroundTaskService.doSomething();
    }
}
