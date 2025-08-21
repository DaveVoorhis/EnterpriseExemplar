package org.reldb.exemplars.java.backend.persistence.two;

import org.reldb.exemplars.java.backend.model.two.Demo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoRepository extends CrudRepository<Demo, Long> {
}
