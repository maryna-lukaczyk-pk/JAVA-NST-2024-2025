package com.example.projectmanagerapp.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.ActiveProfiles;

/**
 * Bazowa klasa dla WSZYSTKICH testów integracyjnych.
 * Zapewnia wspólny kontener PostgreSQL i automatyczną konfigurację Spring Boot.
 */

@ActiveProfiles("integration-test")
@Testcontainers
@SpringBootTest
public abstract class BaseIT {


    // Uruchomienie kontenera bazy danych (PostgreSQL w tym przykładzie) dla testów
    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");


    @DynamicPropertySource
    private static void overrideProps(DynamicPropertyRegistry registry) {
        // Po uruchomieniu kontenera, rejestrujemy aktualne wartości URL, username, password
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }
}
