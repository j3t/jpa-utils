package com.github.j3t.jpa.utils.eclipselink;

import org.springframework.test.context.ContextConfiguration;

import com.github.j3t.jpa.utils.test.EntityManagerHelperImplTest;
import com.github.j3t.jpa.utils.test.HSQLConfig;
import com.github.j3t.jpa.utils.test.JpaConfig;

@ContextConfiguration(classes = { HSQLConfig.class, JpaConfig.class, EclipselinkConfig.class })
public class EclipselinkEntityManagerHelperHSQLTest extends EntityManagerHelperImplTest
{

}
