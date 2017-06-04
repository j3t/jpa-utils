
package com.github.j3t.jpa.utils.test.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.vendor.Database;

import javax.sql.DataSource;

@Configuration
@Profile("mysql")
public class MySQLConfig
{
    @Bean
    public Database dialect()
    {
        return Database.MYSQL;
    }

    @Bean
    public DataSource dataSource(ResourceLoader resourceLoader)
    {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName("com.mysql.jdbc.Driver");
        driver.setUrl("jdbc:mysql://localhost:3306/jpa_utils");
        driver.setUsername("mysql");
        driver.setPassword("s3cr3t");

        ResourceDatabasePopulator db = new ResourceDatabasePopulator();
        db.addScript(resourceLoader.getResource("/create-db.sql"));
        db.execute(driver);
        
        return driver;
    }
}
