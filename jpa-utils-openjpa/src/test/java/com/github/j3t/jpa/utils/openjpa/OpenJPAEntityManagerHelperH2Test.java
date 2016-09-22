package com.github.j3t.jpa.utils.openjpa;

import org.springframework.test.context.ContextConfiguration;

import com.github.j3t.jpa.utils.test.EntityManagerHelperImplTest;
import com.github.j3t.jpa.utils.test.H2Config;
import com.github.j3t.jpa.utils.test.JpaConfig;

/**
 * You have to provide the javaagent as argument to the jvm running this test. (e.g. java ...
 * -javaagent:~/.m2/repository/org/apache/openjpa/openjpa/2.4.1/openjpa-2.4.1.jar
 * 
 * @author j3t
 */
@ContextConfiguration(classes = { H2Config.class, JpaConfig.class, OpenJPAConfig.class })
public class OpenJPAEntityManagerHelperH2Test extends EntityManagerHelperImplTest
{
}
