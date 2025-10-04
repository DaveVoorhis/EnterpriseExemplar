package org.reldb.exemplars.java.backend.persistence;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoSchemaMigrator {
    @Autowired
    DemoConfiguration config;

    @Bean
    public SpringLiquibase liquibaseTwo() {
        final var liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:liquibase/demo.xml");
        liquibase.setDataSource(config.demoDataSource());
        return liquibase;
    }
}
