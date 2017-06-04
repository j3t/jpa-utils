
package com.github.j3t.jpa.utils.test.config;


import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.vendor.Database;

@Configuration
@Profile("derby")
public class DerbyConfig
{
    @Bean
    public Database dialect()
    {
        return Database.DERBY;
    }
    
    @Bean
    public DataSource dataSource()
    {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.DERBY)
                .addScript("/create-db.sql")
                .build();
    }
}
