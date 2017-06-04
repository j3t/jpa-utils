
package com.github.j3t.jpa.utils.test.config;


import java.util.Properties;

import javax.sql.DataSource;

import com.github.j3t.jpa.utils.eclipselink.EclipselinkEntityManagerHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

import com.github.j3t.jpa.utils.core.EntityManagerHelper;
import com.github.j3t.jpa.utils.test.domain.User;

@Configuration
public class EclipselinkConfig
{
    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Database database) throws Exception
    {
        EclipseLinkJpaVendorAdapter jpaVendorAdapter = new EclipseLinkJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(false);
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setDatabase(database);

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("eclipselink.weaving", "false");

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setPersistenceUnitName("jpaUtilsEclipselinkPU");
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan(User.class.getPackage().getName());
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean
    EntityManagerHelper entityManagerHelper()
    {
        return new EclipselinkEntityManagerHelper();
    }
    
}
