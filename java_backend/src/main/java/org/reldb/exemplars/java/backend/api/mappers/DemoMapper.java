package org.reldb.exemplars.java.backend.api.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.reldb.exemplars.java.backend.api.model.DemoOut;
import org.reldb.exemplars.java.backend.model.demo.Demo;

@Mapper
public interface DemoMapper {
    List<DemoOut> toDemoOut(List<Demo> demos);
    DemoOut toDemoOut(Demo demo);
}
