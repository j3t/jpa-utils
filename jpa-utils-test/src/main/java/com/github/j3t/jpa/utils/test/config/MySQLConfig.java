
package com.github.j3t.jpa.utils.test.config;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.command.PullImageResultCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import org.springframework.orm.jpa.vendor.Database;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

@Configuration
@Import(DockerConfig.class)
@Profile("mysql")
public class MySQLConfig {
    private static final String IMAGE_ID = "mysql:5.7";

    @Bean
    public Database dialect() {
        return Database.MYSQL;
    }

    @Bean
    public CreateContainerResponse container(DockerClient dockerClient) throws InterruptedException {
        // pull MySQL image
        dockerClient
                .pullImageCmd(IMAGE_ID)
                .exec(new PullImageResultCallback())
                .awaitSuccess();

        // create MySQL container
        return dockerClient.createContainerCmd(IMAGE_ID)
                .withPortBindings(new Ports(ExposedPort.tcp(3306), Ports.Binding.empty()))
                .withEnv("MYSQL_USER=mysql", "MYSQL_PASSWORD=mysql", "MYSQL_DATABASE=test", "MYSQL_ROOT_PASSWORD=root")
                .exec();
    }

    @Bean
    public DataSource dataSource(@Value("/create-db.sql") Resource databaseScript, DockerClient dockerClient, CreateContainerResponse container) throws Exception {
        // start MySQL container
        dockerClient.startContainerCmd(container.getId()).exec();

        // find MySQL listener (mapped port of exposed port 3306)
        String exposedPort = dockerClient
                .inspectContainerCmd(container.getId())
                .exec()
                .getNetworkSettings()
                .getPorts()
                .getBindings()
                .get(ExposedPort.tcp(3306))[0]
                .getHostPortSpec();

        // create DataSource to connect to the MySQL container
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName("com.mysql.jdbc.Driver");
        driver.setUrl("jdbc:mysql://localhost:" + exposedPort + "/test");
        driver.setUsername("mysql");
        driver.setPassword("mysql");

        // create DatabasePopulator to initialize the MySQL container
        ResourceDatabasePopulator db = new ResourceDatabasePopulator();
        db.addScript(databaseScript);
        // execute DatabasePopulator
        int count = 10;
        while (count > 0) {
            try {
                // try execute
                db.execute(driver);
                // when successful -> done
                count = 0;
            } catch (UncategorizedScriptException e) {
                // when failed -> try again
                count--;
                Thread.sleep(5000);
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
        // stop MySQL container
        dockerClient.stopContainerCmd(container.getId()).exec();
    }

}
