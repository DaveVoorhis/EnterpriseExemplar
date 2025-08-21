package org.reldb.exemplars.java.backend.service;

import org.reldb.exemplars.java.backend.ApplicationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TestPeriodicBackgroundTaskService extends ApplicationTestBase {
    @Autowired
    PeriodicBackgroundTaskService periodicBackgroundTaskService;

    @Test
    void ensureThereIsAMethodToCall() {
        periodicBackgroundTaskService.doSomething();
    }
}
