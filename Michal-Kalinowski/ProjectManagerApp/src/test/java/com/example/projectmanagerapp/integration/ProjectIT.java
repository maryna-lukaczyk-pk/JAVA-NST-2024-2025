package com.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProjectIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void datasourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Long projectId;
    private static Long userId;

    @Test
    @Order(1)
    void createUserAndProject() throws Exception {
        String userJson = """
            {
              "username": "bob",
              "email": "bob@example.com"
            }
        """;

        var userResult = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andReturn();
        userId = objectMapper.readTree(userResult.getResponse().getContentAsString()).get("id").asLong();

        String projectJson = """
            {
              "name": "Projekt Testowy"
            }
        """;

        var projectResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isCreated())
                .andReturn();
        projectId = objectMapper.readTree(projectResult.getResponse().getContentAsString()).get("id").asLong();
    }

    @Test
    @Order(2)
    void assignUserToProject() throws Exception {
        mockMvc.perform(post("/api/projects/" + projectId + "/users")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void verifyUserAssigned() throws Exception {
        var result = mockMvc.perform(get("/api/projects/" + projectId + "/users"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        System.out.println("Response: " + json);

        assertThat(json).contains("\"id\":" + userId); // prosta asercja tekstowa
    }
}
