package com.example.projectmanagerapp;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.TaskRepository;
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
public class ProjectTaskIT {
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
    private TaskRepository taskRepository;
    @Test
    public void shouldAssignTaskToProjectAndVerifyRelation() throws Exception {
        // Create example task
        Task task = new Task();
        task.setTitle("Example task");
        task.setDescription("Task description");

        String taskResponse = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Example task"))
                .andReturn().getResponse().getContentAsString();

        long taskId = objectMapper.readTree(taskResponse).get("id").asLong();

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

        // Add task to project
        mockMvc.perform(post("/api/projects/" + projectId + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("taskId", taskId))))
                .andExpect(status().isOk());

        // Verify task is in project's tasks list
        mockMvc.perform(get("/api/projects/" + projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasks", hasSize(1)))
                .andExpect(jsonPath("$.tasks[*].id", containsInAnyOrder((int) taskId)));
    }

    @Test
    void shouldThrowExceptionWhenAssigningTaskToNonExistentProject() {
        Task task = new Task();
        task.setTitle("Real Task");
        Task savedTask = taskRepository.save(task);

        Long nonExistentProjectId = 777L;

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                projectService.assignTaskToProject(savedTask.getId(), nonExistentProjectId));

        assertTrue(exception.getMessage().contains("Project with id " + nonExistentProjectId + " not found"));
    }

    @Test
    void shouldThrowExceptionWhenAssigningNonExistentTaskToProject() {
        Project project = new Project();
        project.setName("Target Project");
        Project savedProject = projectRepository.save(project);

        Long nonExistentTaskId = 666L;

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                projectService.assignTaskToProject(nonExistentTaskId, savedProject.getId()));

        assertTrue(exception.getMessage().contains("Task with id " + nonExistentTaskId + " not found"));
    }
}
