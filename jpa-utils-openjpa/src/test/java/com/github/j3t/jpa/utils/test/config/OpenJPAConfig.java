
package com.github.j3t.jpa.utils.test.config;


import javax.sql.DataSource;

import com.github.j3t.jpa.utils.openjpa.OpenJPAEntityManagerHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;

import com.github.j3t.jpa.utils.core.EntityManagerHelper;

@Configuration
public class OpenJPAConfig
{
    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Database database) throws Exception
    {
        OpenJpaVendorAdapter jpaVendorAdapter = new OpenJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(false);
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setDatabase(database);

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setPersistenceUnitName("jpaUtilsOpenJPAPU");
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactoryBean.setPackagesToScan("com.github.j3t.jpa.utils.test.domain");

        return entityManagerFactoryBean;
    }

    @Bean
    EntityManagerHelper entityManagerHelper()
    {
        return new OpenJPAEntityManagerHelper();
    }
    
}
