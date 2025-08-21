package org.reldb.exemplars.java.backend.persistence;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MainSchemaMigrator {
    @Autowired
    MainConfiguration config;

    @Primary
    @Bean
    public SpringLiquibase liquibaseMain() {
        final var liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:liquibase/main.xml");
        liquibase.setDataSource(config.mainDataSource());
        return liquibase;
    }
}
