package com.example.projectmanagerapp.integration;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@Testcontainers
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                // wymuszamy schemat create-drop jeszcze przed uruchomieniem kontenera
                "spring.jpa.hibernate.ddl-auto=create-drop"
        }
)
@AutoConfigureMockMvc
@DirtiesContext(classMode = AFTER_CLASS)
public abstract class BaseIT {

    @Container
    public static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        // nadpisujemy URL/creds/driver po starcie kontenera
        registry.add("spring.datasource.url",               postgres::getJdbcUrl);
        registry.add("spring.datasource.username",          postgres::getUsername);
        registry.add("spring.datasource.password",          postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }
}
