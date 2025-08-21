package org.reldb.exemplars.java.backend.persistence;

import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "org.reldb.exemplars.java.backend.persistence.main", entityManagerFactoryRef = "mainEntityManager", transactionManagerRef = "mainTransactionManager")
public class MainConfiguration extends BaseConfiguration {
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean mainEntityManager() {
        return entityManager("main", mainDataSource(), "org.reldb.exemplars.java.backend.model.main");
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource mainDataSource() {
        return dataSource();
    }

    @Primary
    @Bean
    public PlatformTransactionManager mainTransactionManager() {
        return transactionManager(mainEntityManager());
    }
}
