
package com.github.j3t.jpa.utils.test.config;


import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.vendor.Database;

@Configuration
@Profile("postgresql")
public class PostgreSQLConfig
{
    @Bean
    public Database dialect()
    {
        return Database.POSTGRESQL;
    }

    @Bean
    public DataSource dataSource(ResourceLoader resourceLoader)
    {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName("org.postgresql.Driver");
        driver.setUrl("jdbc:postgresql://localhost:5432/jpa_utils");
        driver.setUsername("postgres");
        driver.setPassword("s3cr3t");

        ResourceDatabasePopulator db = new ResourceDatabasePopulator();
        db.addScript(resourceLoader.getResource("/create-db.sql"));
        db.execute(driver);
        
        return driver;
    }
}
