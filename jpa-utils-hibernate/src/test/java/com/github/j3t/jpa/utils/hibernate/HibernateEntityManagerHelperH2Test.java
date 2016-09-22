package com.github.j3t.jpa.utils.hibernate;

import org.springframework.test.context.ContextConfiguration;

import com.github.j3t.jpa.utils.test.EntityManagerHelperImplTest;
import com.github.j3t.jpa.utils.test.H2Config;
import com.github.j3t.jpa.utils.test.JpaConfig;

@ContextConfiguration(classes = { H2Config.class, JpaConfig.class, HibernateConfig.class })
public class HibernateEntityManagerHelperH2Test extends EntityManagerHelperImplTest
{

}
