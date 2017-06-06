
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
@Profile("oracle")
public class OracleConfig {
    private static final String IMAGE_ID = "wnameless/oracle-xe-11g:16.04";

    @Bean
    public Database dialect() {
        return Database.ORACLE;
    }

    @Bean
    public CreateContainerResponse container(DockerClient dockerClient) throws InterruptedException {
        // pull OracleXE image
        dockerClient
                .pullImageCmd(IMAGE_ID)
                .exec(new PullImageResultCallback())
                .awaitSuccess();

        // create OracleXE container
        return dockerClient.createContainerCmd(IMAGE_ID)
                .withPortBindings(new Ports(ExposedPort.tcp(1521), Ports.Binding.empty()))
                .exec();
    }

    @Bean
    public DataSource dataSource(@Value("/create-db.sql") Resource databaseScript, DockerClient dockerClient, CreateContainerResponse container) throws Exception {
        // start OracleXE container
        dockerClient.startContainerCmd(container.getId()).exec();

        // find OracleXE listener (mapped port of exposed port 1521)
        String exposedPort = dockerClient
                .inspectContainerCmd(container.getId())
                .exec()
                .getNetworkSettings()
                .getPorts()
                .getBindings()
                .get(ExposedPort.tcp(1521))[0]
                .getHostPortSpec();

        // create DataSource to connect to the OracleXE container
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@localhost:" + exposedPort + ":xe");
        dataSource.setUsername("system");
        dataSource.setPassword("oracle");

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
        // stop OracleXE container
        dockerClient.stopContainerCmd(container.getId()).exec();
    }

}
