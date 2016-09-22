package com.github.j3t.jpa.utils.eclipselink;

import org.springframework.test.context.ContextConfiguration;

import com.github.j3t.jpa.utils.test.EntityManagerHelperImplTest;
import com.github.j3t.jpa.utils.test.H2Config;
import com.github.j3t.jpa.utils.test.JpaConfig;

@ContextConfiguration(classes = { H2Config.class, JpaConfig.class, EclipselinkConfig.class })
public class EclipselinkEntityManagerHelperH2Test extends EntityManagerHelperImplTest
{

}
