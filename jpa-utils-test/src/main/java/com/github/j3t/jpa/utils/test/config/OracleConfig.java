
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
@Profile("oracle")
public class OracleConfig
{
    @Bean
    public Database dialect()
    {
        return Database.ORACLE;
    }

    @Bean
    public DataSource dataSource(ResourceLoader resourceLoader)
    {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        driver.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        driver.setUsername("system");
        driver.setPassword("oracle");

        ResourceDatabasePopulator db = new ResourceDatabasePopulator();
        db.addScript(resourceLoader.getResource("/create-db.sql"));
        db.execute(driver);
        
        return driver;
    }
}
