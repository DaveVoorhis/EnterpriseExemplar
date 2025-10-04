package org.reldb.exemplars.java.backend.persistence.demo;

import org.reldb.exemplars.java.backend.model.demo.Demo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoRepository extends CrudRepository<Demo, Long> {
}
