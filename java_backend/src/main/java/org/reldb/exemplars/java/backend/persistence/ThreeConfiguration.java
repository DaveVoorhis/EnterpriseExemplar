package org.reldb.exemplars.java.backend.persistence;

import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "org.reldb.exemplars.java.backend.persistence.three", entityManagerFactoryRef = "threeEntityManager", transactionManagerRef = "threeTransactionManager")
public class ThreeConfiguration extends BaseConfiguration {
    @Bean
    public LocalContainerEntityManagerFactoryBean threeEntityManager() {
        return entityManager("three", threeDataSource(), "org.reldb.exemplars.java.backend.model.three");
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-three")
    public DataSource threeDataSource() {
        return dataSource();
    }

    @Bean
    public PlatformTransactionManager threeTransactionManager() {
        return transactionManager(threeEntityManager());
    }
}
