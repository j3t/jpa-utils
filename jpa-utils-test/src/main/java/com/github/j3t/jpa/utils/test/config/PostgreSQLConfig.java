
package com.github.j3t.jpa.utils.test.config;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.command.PullImageResultCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import org.springframework.orm.jpa.vendor.Database;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Import(DockerConfig.class)
@Profile("postgresql")
public class PostgreSQLConfig {
    private static final String IMAGE_ID = "postgres:9.6";

    @Bean
    public Database dialect() {
        return Database.POSTGRESQL;
    }

    @Bean
    public CreateContainerResponse container(DockerClient dockerClient) throws InterruptedException {
        dockerClient
                .pullImageCmd(IMAGE_ID)
                .exec(new PullImageResultCallback())
                .awaitSuccess();

        // create container
        return dockerClient.createContainerCmd(IMAGE_ID)
                .withPortBindings(new Ports(ExposedPort.tcp(5432), Ports.Binding.empty()))
                .withEnv("POSTGRES_PASSWORD=postgres", "POSTGRES_DB=test")
                .exec();
    }

    @Bean
    public DataSource dataSource(@Value("/create-db.sql") Resource databaseScript, DockerClient dockerClient, CreateContainerResponse container) throws Exception {
        // start PostgreSQL container
        dockerClient.startContainerCmd(container.getId()).exec();

        // find PostgreSQL listener (mapped port of exposed port 5432)
        String exposedPort = dockerClient
                .inspectContainerCmd(container.getId())
                .exec()
                .getNetworkSettings()
                .getPorts()
                .getBindings()
                .get(ExposedPort.tcp(5432))[0]
                .getHostPortSpec();

        // create DataSource to connect to the PostgreSQL container
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName("org.postgresql.Driver");
        driver.setUrl("jdbc:postgresql://localhost:" + exposedPort + "/test");
        driver.setUsername("postgres");
        driver.setPassword("postgres");

        // create DatabasePopulator to initialize the PostgreSQL container
        ResourceDatabasePopulator db = new ResourceDatabasePopulator();
        db.addScript(databaseScript);
        // execute DatabasePopulator
        int count = 10;
        while (count > 0) {
            try {
                db.execute(driver);
                count = 0;
            } catch (UncategorizedScriptException e) {
                count--;
                Thread.sleep(500);
            }
        }

        return driver;
    }

    @Autowired
    CreateContainerResponse container;

    @Autowired
    DockerClient dockerClient;

    @PreDestroy
    public void stopContainer() {
        // stop PostgreSQL container
        dockerClient.stopContainerCmd(container.getId()).exec();
    }

}
