package org.reldb.exemplars.java.backend.api.mappers;

import org.reldb.exemplars.java.backend.api.model.DemoOut;
import org.reldb.exemplars.java.backend.model.two.Demo;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface DemoMapper {
    List<DemoOut> toDemoOut(List<Demo> demos);
    DemoOut toDemoOut(Demo demo);
}
