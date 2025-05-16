package com.example.projectmanagerapp;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


/**
 * Bazowa klasa dla WSZYSTKICH testów integracyjnych.
 * Zapewnia wspólny kontener PostgreSQL i automatyczną konfigurację Spring Boot.
 */
@Testcontainers
@SpringBootTest
public abstract class AbstractPostgresIT {

    @Container                                              // kontener startuje raz
    @ServiceConnection                                      // Boot podmienia datasource.*
    protected static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb_shared") // Używam innej nazwy dla pewności, że to ten kontener
                    .withUsername("testuser_shared")
                    .withPassword("testpass_shared");

    // Wspólna konfiguracja dla wszystkich testów IT dziedziczących z tej klasy
    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        // @ServiceConnection już konfiguruje URL, username, password, driver.
        // Dodajemy ddl-auto dla spójności w testach.
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

}
