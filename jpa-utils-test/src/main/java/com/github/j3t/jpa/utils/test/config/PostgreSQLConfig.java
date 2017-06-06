
package com.github.j3t.jpa.utils.test.config;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.j3t.jpa.utils.core.DataSourceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.vendor.Database;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

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
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:" + exposedPort + "/test");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");

        DataSourceHelper.waitUntilDatabaseIsAvailable(dataSource, 600);

        // create DatabasePopulator to initialize the PostgreSQL container
        ResourceDatabasePopulator db = new ResourceDatabasePopulator();
        db.addScript(databaseScript);
        db.execute(dataSource);

        return dataSource;
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
