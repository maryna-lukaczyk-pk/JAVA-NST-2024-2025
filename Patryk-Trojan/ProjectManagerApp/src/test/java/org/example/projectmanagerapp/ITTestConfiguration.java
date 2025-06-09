package org.example.projectmanagerapp;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

@TestConfiguration
public class ITTestConfiguration {
    @Bean
    public PostgreSQLContainer<?> postgresContainer() {
        PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:12.8"));
        postgresContainer.start();
        return postgresContainer;
    }

    @Bean
    @Primary
    public DataSource dataSource(final PostgreSQLContainer<?> postgresContainer) {
        return DataSourceBuilder.create()
                .url(postgresContainer.getJdbcUrl())
                .username(postgresContainer.getUsername())
                .password(postgresContainer.getPassword())
                .driverClassName(postgresContainer.getDriverClassName())
                .build();
    }
}
