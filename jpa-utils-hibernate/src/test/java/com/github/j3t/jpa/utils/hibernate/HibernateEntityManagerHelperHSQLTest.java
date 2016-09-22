package com.github.j3t.jpa.utils.hibernate;

import org.springframework.test.context.ContextConfiguration;

import com.github.j3t.jpa.utils.test.EntityManagerHelperImplTest;
import com.github.j3t.jpa.utils.test.HSQLConfig;
import com.github.j3t.jpa.utils.test.JpaConfig;

@ContextConfiguration(classes = { HSQLConfig.class, JpaConfig.class, HibernateConfig.class })
public class HibernateEntityManagerHelperHSQLTest extends EntityManagerHelperImplTest
{

}
