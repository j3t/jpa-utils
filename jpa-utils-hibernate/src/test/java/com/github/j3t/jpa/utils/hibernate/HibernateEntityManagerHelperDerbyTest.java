package com.github.j3t.jpa.utils.hibernate;

import org.springframework.test.context.ContextConfiguration;

import com.github.j3t.jpa.utils.test.EntityManagerHelperImplTest;
import com.github.j3t.jpa.utils.test.DerbyConfig;
import com.github.j3t.jpa.utils.test.JpaConfig;

@ContextConfiguration(classes = { DerbyConfig.class, JpaConfig.class, HibernateConfig.class })
public class HibernateEntityManagerHelperDerbyTest extends EntityManagerHelperImplTest
{

}
