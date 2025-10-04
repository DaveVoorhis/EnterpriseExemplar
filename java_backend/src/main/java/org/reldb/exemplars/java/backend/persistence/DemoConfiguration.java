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
        basePackages = "org.reldb.exemplars.java.backend.persistence.demo",
        entityManagerFactoryRef = "demoEntityManager",
        transactionManagerRef = "demoTransactionManager")
public class DemoConfiguration extends BaseConfiguration {
    @Bean
    public LocalContainerEntityManagerFactoryBean demoEntityManager() {
        return entityManager("demo", demoDataSource(), "org.reldb.exemplars.java.backend.model.demo");
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-demo")
    public DataSource demoDataSource() {
        return dataSource();
    }

    @Bean
    public PlatformTransactionManager demoTransactionManager() {
        return transactionManager(demoEntityManager());
    }
}
