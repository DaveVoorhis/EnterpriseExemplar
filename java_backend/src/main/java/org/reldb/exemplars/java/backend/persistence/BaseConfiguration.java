package org.reldb.exemplars.java.backend.persistence;

import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public abstract class BaseConfiguration {
    @Autowired
    private Environment env;

    protected LocalContainerEntityManagerFactoryBean entityManager(String name,
                                                                   DataSource dataSource,
                                                                   String... packages) {
        final var entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setPersistenceUnitName(name);
        entityManager.setDataSource(dataSource);
        entityManager.setPackagesToScan(packages);

        final var vendorAdapter = new HibernateJpaVendorAdapter();
        entityManager.setJpaVendorAdapter(vendorAdapter);
        final var properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        entityManager.setJpaPropertyMap(properties);

        return entityManager;
    }

    protected DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    protected PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManager) {
        final var transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager.getObject());
        return transactionManager;
    }
}
