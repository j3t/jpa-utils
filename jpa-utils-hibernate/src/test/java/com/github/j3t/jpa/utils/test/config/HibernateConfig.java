
package com.github.j3t.jpa.utils.test.config;


import javax.sql.DataSource;

import com.github.j3t.jpa.utils.hibernate.HibernateEntityManagerHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.github.j3t.jpa.utils.core.EntityManagerHelper;
import com.github.j3t.jpa.utils.test.domain.User;

@Configuration
public class HibernateConfig
{
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Database database) throws Exception
    {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(false);
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setDatabase(database);

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setPersistenceUnitName("jpaUtilsHibernatePU");
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan(User.class.getPackage().getName());
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

        return entityManagerFactoryBean;
    }

    @Bean
    public EntityManagerHelper entityManagerHelper()
    {
        return new HibernateEntityManagerHelper();
    }
    
}
