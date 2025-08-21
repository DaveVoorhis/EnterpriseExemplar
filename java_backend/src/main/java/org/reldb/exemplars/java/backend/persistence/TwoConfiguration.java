package org.reldb.exemplars.java.backend.persistence;

import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "org.reldb.exemplars.java.backend.persistence.two", entityManagerFactoryRef = "twoEntityManager", transactionManagerRef = "twoTransactionManager")
public class TwoConfiguration extends BaseConfiguration {
    @Bean
    public LocalContainerEntityManagerFactoryBean twoEntityManager() {
        return entityManager("two", twoDataSource(), "org.reldb.exemplars.java.backend.model.two");
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-two")
    public DataSource twoDataSource() {
        return dataSource();
    }

    @Bean
    public PlatformTransactionManager twoTransactionManager() {
        return transactionManager(twoEntityManager());
    }
}
