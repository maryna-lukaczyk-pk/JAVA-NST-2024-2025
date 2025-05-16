package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.projectmanagerapp.AbstractPostgresIT;

/**
 * Testy integracyjne dla endpointów /api/users.
 * Sprawdzają operacje CRUD (GET, POST, PUT, DELETE) na encji User.
 */
@SpringBootTest
@AutoConfigureMockMvc
//@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class UserIT extends AbstractPostgresIT {

    /*@Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }*/

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testUtworzenieNowegoUzytkownika() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        String userJson = objectMapper.writeValueAsString(user);

        // Test POST /api/users - tworzenie użytkownika
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    public void testPobranieWszystkichUzytkownikow() throws Exception {
        // Dodanie dwóch użytkowników
        User user1 = new User();
        user1.setUsername("user1");
        String userJson1 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(status().isOk());

        User user2 = new User();
        user2.setUsername("user2");
        String userJson2 = objectMapper.writeValueAsString(user2);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andExpect(status().isOk());

        // Test GET /api/users - pobranie wszystkich użytkowników
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testPobranieUzytkownikaPoId() throws Exception {
        // Utworzenie jednego użytkownika
        User user = new User();
        user.setUsername("singleUser");
        String userJson = objectMapper.writeValueAsString(user);
        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        User createdUser = objectMapper.readValue(response, User.class);

        // Test GET /api/users/{id} - pobranie użytkownika po ID
        mockMvc.perform(get("/api/users/{id}", createdUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("singleUser")));
    }

    @Test
    public void testAktualizacjaUzytkownika() throws Exception {
        // Utworzenie użytkownika
        User user = new User();
        user.setUsername("oldName");
        String userJson = objectMapper.writeValueAsString(user);
        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        User createdUser = objectMapper.readValue(response, User.class);

        // Aktualizacja użytkownika
        User updated = new User();
        updated.setUsername("newName");
        String updateJson = objectMapper.writeValueAsString(updated);
        mockMvc.perform(put("/api/users/{id}", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("newName")));
    }

    @Test
    public void testUsuwanieUzytkownika() throws Exception {
        // Utworzenie użytkownika do usunięcia
        User user = new User();
        user.setUsername("toDelete");
        String userJson = objectMapper.writeValueAsString(user);
        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        User createdUser = objectMapper.readValue(response, User.class);

        // Test DELETE /api/users/{id} - usuwanie użytkownika
        mockMvc.perform(delete("/api/users/{id}", createdUser.getId()))
                .andExpect(status().isNoContent());
    }
}
