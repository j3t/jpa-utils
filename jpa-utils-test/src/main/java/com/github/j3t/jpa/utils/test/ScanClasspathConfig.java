package com.github.j3t.jpa.utils.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by Jens Thielscher on 04.06.2017.
 */
@ComponentScan(basePackages = "com.github.j3t.jpa.utils.test.config")
@Configuration
public class ScanClasspathConfig {

}
