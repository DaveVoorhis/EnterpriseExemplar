package org.reldb.exemplars.java.backend.persistence;

import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class UserSchemaMigrator {
    private final UserConfiguration config;

    @Primary
    @Bean
    public SpringLiquibase liquibaseMain() {
        final var liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:liquibase/user.xml");
        liquibase.setDataSource(config.userDataSource());
        return liquibase;
    }
}
