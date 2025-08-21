package org.reldb.exemplars.java.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PeriodicBackgroundTaskService {
    public void doSomething() {
        log.info("Running periodic background task.");
    }
}
