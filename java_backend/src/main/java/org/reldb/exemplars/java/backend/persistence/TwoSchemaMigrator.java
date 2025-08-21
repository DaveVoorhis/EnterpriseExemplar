package org.reldb.exemplars.java.backend.persistence;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwoSchemaMigrator {
    @Autowired
    TwoConfiguration config;

    @Bean
    public SpringLiquibase liquibaseTwo() {
        final var liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:liquibase/two.xml");
        liquibase.setDataSource(config.twoDataSource());
        return liquibase;
    }
}
