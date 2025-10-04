package org.reldb.exemplars.java.backend.service;

import org.reldb.exemplars.java.backend.exception.custom.DemoNotFoundException;
import org.reldb.exemplars.java.backend.model.demo.Demo;
import org.reldb.exemplars.java.backend.persistence.demo.DemoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

@Service
public class DemoService {
    @Autowired
    private DemoRepository demoRepository;

    public List<Demo> getAllDemos() {
        return Streamable.of(demoRepository.findAll()).toList();
    }

    public Demo getDemo(long demoId) {
        return findDemo(demoId);
    }

    public Demo addDemo(String name, String address) {
        return saveDemo(new Demo(), name, address);
    }

    public void updateDemo(long demoId, String name, String address) {
        final var demo = findDemo(demoId);
        saveDemo(demo, name, address);
    }

    public void deleteDemo(long demoId) {
        findDemo(demoId);
        demoRepository.deleteById(demoId);
    }

    private Demo findDemo(long demoId) {
        return demoRepository.findById(demoId)
                .orElseThrow(() -> new DemoNotFoundException(demoId));
    }

    private Demo saveDemo(Demo demo, String name, String address) {
        demo.setName(name);
        demo.setAddress(address);
        return demoRepository.save(demo);
    }
}
