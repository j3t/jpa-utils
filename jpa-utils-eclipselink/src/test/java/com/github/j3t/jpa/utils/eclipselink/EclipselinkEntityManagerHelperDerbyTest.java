package com.github.j3t.jpa.utils.eclipselink;

import org.springframework.test.context.ContextConfiguration;

import com.github.j3t.jpa.utils.test.EntityManagerHelperImplTest;
import com.github.j3t.jpa.utils.test.DerbyConfig;
import com.github.j3t.jpa.utils.test.JpaConfig;

@ContextConfiguration(classes = { DerbyConfig.class, JpaConfig.class, EclipselinkConfig.class })
public class EclipselinkEntityManagerHelperDerbyTest extends EntityManagerHelperImplTest
{

}
