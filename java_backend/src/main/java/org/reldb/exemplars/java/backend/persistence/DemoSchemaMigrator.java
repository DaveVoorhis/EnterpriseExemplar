package org.reldb.exemplars.java.backend.persistence;

import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DemoSchemaMigrator {
    private final DemoConfiguration config;

    @Bean
    public SpringLiquibase liquibaseTwo() {
        final var liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:liquibase/demo.xml");
        liquibase.setDataSource(config.demoDataSource());
        return liquibase;
    }
}
