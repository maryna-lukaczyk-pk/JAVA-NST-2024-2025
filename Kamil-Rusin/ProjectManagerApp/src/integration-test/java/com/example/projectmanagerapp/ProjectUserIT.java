package com.example.projectmanagerapp;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.UserRepository;
import com.example.projectmanagerapp.services.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
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

import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(classes = ProjectManagerAppApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ProjectUserIT {
    @Container
    static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("test_db")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> "true");
        registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void shouldAssignUserToProjectAndVerifyRelation() throws Exception {
        // Create example user
        User user = new User();
        user.setUserName("Example User");

        String userResponse = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Example User"))
                .andReturn().getResponse().getContentAsString();

        long userId = objectMapper.readTree(userResponse).get("id").asLong();

        // Create example project
        Project project = new Project();
        project.setName("Example project");

        String projectResponse = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Example project"))
                .andReturn().getResponse().getContentAsString();

        long projectId = objectMapper.readTree(projectResponse).get("id").asLong();

        // Add user to project
        mockMvc.perform(post("/api/projects/" + projectId + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("userId", userId))))
                .andExpect(status().isOk());

        // Verify user is in project's members list
        mockMvc.perform(get("/api/projects/" + projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users[*].id", containsInAnyOrder((int)userId)));
    }

    @Test
    void shouldThrowExceptionWhenAssigningUserToNonExistentProject() {
        User user = new User();
        user.setId(1L);
        user.setUserName("Ghost");
        User savedUser = userRepository.save(user);

        Long nonExistentProjectId = 999L;

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                projectService.assignUserToProject(savedUser.getId(), nonExistentProjectId));

        assertTrue(exception.getMessage().contains("Project with id " + nonExistentProjectId + " not found"));
    }

    @Test
    void shouldThrowExceptionWhenAssigningNonExistentUserToProject() {
        Project project = new Project();
        project.setName("Real Project");

        Project savedProject = projectRepository.save(project);

        Long nonExistentUserId = 888L;

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                projectService.assignUserToProject(nonExistentUserId, savedProject.getId()));

        assertTrue(exception.getMessage().contains("User with id " + nonExistentUserId + " not found"));
    }
}
