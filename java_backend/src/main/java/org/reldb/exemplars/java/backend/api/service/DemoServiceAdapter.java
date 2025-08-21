package org.reldb.exemplars.java.backend.api.service;

import org.reldb.exemplars.java.backend.api.mappers.DemoMapper;
import org.reldb.exemplars.java.backend.api.model.DemoIn;
import org.reldb.exemplars.java.backend.api.model.DemoOut;
import org.reldb.exemplars.java.backend.service.DemoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceAdapter {
    @Autowired
    private DemoService demoService;

    @Autowired
    private DemoMapper demoMapper;

    public List<DemoOut> getAllDemos() {
        return demoMapper.toDemoOut(demoService.getAllDemos());
    }

    public DemoOut getDemo(long demoId) {
        return demoMapper.toDemoOut(demoService.getDemo(demoId));
    }

    public DemoOut addDemo(DemoIn demo) {
        return demoMapper.toDemoOut(demoService.addDemo(demo.name(), demo.address()));
    }

    public void updateDemo(long demoId, DemoIn demoIn) {
        demoService.updateDemo(demoId, demoIn.name(), demoIn.address());
    }

    public void deleteDemo(long demoId) {
        demoService.deleteDemo(demoId);
    }
}
