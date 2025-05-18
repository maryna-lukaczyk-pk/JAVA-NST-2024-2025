package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class ProjectIntegrationTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15.2")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void contextLoads() throws Exception {
        // Prost y test sprawdzający, czy /api/projects w ogóle działa
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateReadUpdateAndDeleteProject() throws Exception {
        // 1) CREATE
        String projectJson = """
            {
              "name": "Test Project"
            }
            """;

        MvcResult createResult = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn();

        // Odczyt odpowiedzi JSON i mapowanie na Project
        String createResponse = createResult.getResponse().getContentAsString();
        Project createdProject = objectMapper.readValue(createResponse, Project.class);

        // 2) READ
        mockMvc.perform(get("/api/projects/{id}", createdProject.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Project"));

        // 3) UPDATE
        String updatedProjectJson = """
            {
              "name": "Updated Project Name"
            }
            """;

        mockMvc.perform(put("/api/projects/{id}", createdProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProjectJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Project Name"));

        // 4) DELETE
        mockMvc.perform(delete("/api/projects/{id}", createdProject.getId()))
                .andExpect(status().isOk());

        // Upewnij się, że projekt został usunięty (np. 404)
        mockMvc.perform(get("/api/projects/{id}", createdProject.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldAssignUserToProject() throws Exception {
        // 1) Tworzymy User
        String userJson = """
            {
              "username": "john_doe"
            }
            """;
        MvcResult createUserResult = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn();

        // Deserializacja do obiektu Users
        Users createdUser = objectMapper.readValue(
                createUserResult.getResponse().getContentAsString(),
                Users.class
        );

        // 2) Tworzymy projekt
        String projectJson = """
            {
              "name": "Test Project 2"
            }
            """;
        MvcResult createProjectResult = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                createProjectResult.getResponse().getContentAsString(),
                Project.class
        );

        // 3) Przypisujemy usera do projektu przez PATCH/POST (zależnie od Twojej implementacji)
        mockMvc.perform(patch("/api/projects/{pId}/users/{uId}",
                        createdProject.getId(), createdUser.getId()))
                .andExpect(status().isOk());

        // 4) Pobieramy projekt z bazy
        MvcResult getProjectResult = mockMvc.perform(get("/api/projects/{id}", createdProject.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Project updatedProject = objectMapper.readValue(
                getProjectResult.getResponse().getContentAsString(),
                Project.class
        );

        // 5) Weryfikacja, czy dany user jest w liście
        assertThat(updatedProject.getUsers())
                .extracting("username")
                .contains("john_doe");
    }
}
