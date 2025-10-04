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
@EnableJpaRepositories(
        basePackages = "org.reldb.exemplars.java.backend.persistence.user", 
        entityManagerFactoryRef = "userEntityManager", 
        transactionManagerRef = "userTransactionManager")
public class UserConfiguration extends BaseConfiguration {
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean userEntityManager() {
        return entityManager("user", userDataSource(), "org.reldb.exemplars.java.backend.model.user");
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource userDataSource() {
        return dataSource();
    }

    @Primary
    @Bean
    public PlatformTransactionManager userTransactionManager() {
        return transactionManager(userEntityManager());
    }
}
