package com.zygimantus.apiwebas.config;

import com.zygimantus.apiwebas.TestDataInitializer;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * Integration testing specific configuration - creates a in-memory datasource,
 * sets hibernate on create drop mode and inserts some test data on the
 * database.
 *
 * This allows to clone the project repository and start a running application
 * with the command
 *
 * mvn clean install tomcat7:run-war -Dspring.profiles.active=test
 *
 * Access http://localhost:8080/ and login with test123 / Password2, in order to
 * see some test data, or create a new user.
 *
 */
@Configuration
@EnableTransactionManagement
public class TestConfiguration {

    @Bean(initMethod = "init")
    public TestDataInitializer initTestData() {
        return new TestDataInitializer();
    }

    @Profile("prod")    
    @Bean(name = "datasource")
    public DriverManagerDataSource prodDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(org.hsqldb.jdbcDriver.class.getName());
        dataSource.setUrl("jdbc:hsqldb:mem:apiwebas");
        dataSource.setUsername("admin");
        dataSource.setPassword("apiwebasAdmin");
        return dataSource;
    }

    @Profile("test")    
    @Bean(name = "datasource")
    public DriverManagerDataSource testDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(org.hsqldb.jdbcDriver.class.getName());
        dataSource.setUrl("jdbc:hsqldb:hsql://localhost:9001/xdb");
        dataSource.setUsername("SA");
        return dataSource;
    }

    @Bean(name = "entityManagerFactory")
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.zygimantus.apiwebas.model");

        sessionFactory.setMappingResources(new String[]{"Swapi.hbm.xml"});

        sessionFactory.getHibernateProperties().put("hibernate.show_sql", "true");
        sessionFactory.getHibernateProperties().put("hibernate.hbm2ddl.auto", "create");
        return sessionFactory;
    }

}
