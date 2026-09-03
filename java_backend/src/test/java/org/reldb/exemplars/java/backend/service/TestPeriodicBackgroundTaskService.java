package org.reldb.exemplars.java.backend.service;

import lombok.RequiredArgsConstructor;
import org.reldb.exemplars.java.backend.ApplicationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class TestPeriodicBackgroundTaskService extends ApplicationTestBase {
    private final PeriodicBackgroundTaskService periodicBackgroundTaskService;

    @Test
    void ensureThereIsAMethodToCall() {
        periodicBackgroundTaskService.doSomething();
    }
}
