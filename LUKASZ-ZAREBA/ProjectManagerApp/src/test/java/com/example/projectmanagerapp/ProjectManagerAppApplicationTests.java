package com.example.projectmanagerapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;


@Testcontainers
@SpringBootTest
class ProjectManagerAppApplicationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");


    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {

    }

    @Test
    void contextLoads() {
    }

    @Test
    void testMainMethod() {
        ProjectManagerAppApplication.main(new String[]{});
    }
}