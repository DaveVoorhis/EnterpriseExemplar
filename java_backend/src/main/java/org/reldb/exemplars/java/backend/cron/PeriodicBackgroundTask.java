package org.reldb.exemplars.java.backend.cron;

import org.reldb.exemplars.java.backend.service.PeriodicBackgroundTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PeriodicBackgroundTask {
    @Autowired
    private PeriodicBackgroundTaskService periodicBackgroundTaskService;

    @Scheduled(cron = "${periodic-background-task.schedule}")
    public void removeExpiredSuspensions() {
        log.debug("Scheduled periodic background task processing.");
        periodicBackgroundTaskService.doSomething();
    }
}
