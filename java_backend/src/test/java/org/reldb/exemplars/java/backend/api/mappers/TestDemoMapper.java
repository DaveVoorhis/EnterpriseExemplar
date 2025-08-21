package org.reldb.exemplars.java.backend.api.mappers;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.reldb.exemplars.java.backend.api.model.DemoOut;
import org.reldb.exemplars.java.backend.model.two.Demo;
import java.util.concurrent.atomic.AtomicInteger;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

public class TestDemoMapper {
    private final DemoMapper mapper = Mappers.getMapper(DemoMapper.class);

    @ParameterizedTest
    @CsvSource({
            "1,blah,blahblah",
            "2,blat,zapzap"
    })
    void toDemoOutTest(long id, String name, String address) {
        final var demo = new Demo();
        ReflectionTestUtils.setField(demo, "demoId", id);
        demo.setName(name);
        demo.setAddress(address);

        final var roleOut = mapper.toDemoOut(demo);

        assertMatch(demo, roleOut);
    }

    @Test
    void toDemosOutTest() {
        final var demos = Instancio.ofList(Demo.class).size(10).create();

        final var demoOuts = mapper.toDemoOut(demos);

        assertThat(demos).hasSameSizeAs(demoOuts);
        final var index = new AtomicInteger(0);
        demos.forEach(role -> assertMatch(role, demoOuts.get(index.getAndIncrement())));
    }

    private void assertMatch(Demo role, DemoOut roleOut) {
        assertThat(role.getDemoId()).isEqualTo(roleOut.demoId());
        assertThat(role.getName()).isEqualTo(roleOut.name());
        assertThat(role.getAddress()).isEqualTo(roleOut.address());
    }
}
