package org.reldb.exemplars.java.backend.persistence;

import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackages = "org.reldb.exemplars.java.backend.persistence.extra",
        entityManagerFactoryRef = "extraEntityManager",
        transactionManagerRef = "extraTransactionManager")
public class ExtraConfiguration extends BaseConfiguration {
    @Bean
    public LocalContainerEntityManagerFactoryBean extraEntityManager() {
        return entityManager("extra", extraDataSource(), "org.reldb.exemplars.java.backend.model.extra");
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-extra")
    public DataSource extraDataSource() {
        return dataSource();
    }

    @Bean
    public PlatformTransactionManager extraTransactionManager() {
        return transactionManager(extraEntityManager());
    }
}
