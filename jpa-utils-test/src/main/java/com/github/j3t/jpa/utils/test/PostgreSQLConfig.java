
package com.github.j3t.jpa.utils.test;


import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.vendor.Database;

@Configuration
public class PostgreSQLConfig
{
    @Bean
    public Database dialect()
    {
        return Database.POSTGRESQL;
    }

    @Bean
    public DataSource dataSource()
    {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName("org.postgresql.Driver");
        driver.setUrl("jdbc:postgresql://localhost:5432/jpa_utils");
        driver.setUsername("postgres");
        driver.setPassword("");
        
        return driver;
    }
}
