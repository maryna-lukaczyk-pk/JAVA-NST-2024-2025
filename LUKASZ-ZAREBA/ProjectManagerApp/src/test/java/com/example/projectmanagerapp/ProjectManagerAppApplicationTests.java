package com.example.projectmanagerapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

/**
 * Uruchamia pełny kontekst Spring Boot z jednorazową bazą PostgreSQL
 * dostarczaną przez TestContainers.  Dzięki adnotacji {@code @ServiceConnection}
 * Spring Boot automatycznie ustawia:
 *
 * spring.datasource.url
 * spring.datasource.username
 * spring.datasource.password
 * spring.datasource.driver-class-name
 */
@Testcontainers
@SpringBootTest
class ProjectManagerAppApplicationTests {

    @Container                                        // ➊ start kontenera tylko raz dla całej klasy
    @ServiceConnection                                // ➋ automatyczna konfiguracja DataSource
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    // (NIE POTRZEBUJESZ ręcznie mapować właściwości – zostawiam,
    //  gdybyś chciał dopisać inne z kontenera)
    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        // registry.add("jakies.inne.wlasciwosci", () -> "wartość");
    }

    @Test
    void contextLoads() {
        // jeżeli kontekst się podniesie – test zalicza się automatycznie
    }
}
