package com.example.projectmanagerapp;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.TaskType;
import com.example.projectmanagerapp.repository.TaskRepository;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    @DisplayName("Should create a new task")
    void testCreateTask() throws Exception {
        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("Description");
        task.setTaskType(TaskType.HIGH_PRIORITY);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.taskType").value("HIGH_PRIORITY"));

        assertTrue(taskRepository.findAll().stream().anyMatch(t -> t.getTitle().equals("New Task")));
    }

    @Test
    @DisplayName("Should get all tasks")
    void testGetAllTasks() throws Exception {
        taskRepository.deleteAll();

        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Desc 1");
        task1.setTaskType(TaskType.LOW_PRIORITY);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Desc 2");
        task2.setTaskType(TaskType.MEDIUM_PRIORITY);
        taskRepository.save(task2);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Should get task by ID")
    void testGetTaskById() throws Exception {
        Task task = new Task();
        task.setTitle("Find Me");
        task.setDescription("Desc");
        task.setTaskType(TaskType.MEDIUM_PRIORITY);
        Task saved = taskRepository.save(task);

        mockMvc.perform(get("/tasks/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Find Me"));
    }

    @Test
    @DisplayName("Should update existing task")
    void testUpdateTask() throws Exception {
        Task task = new Task();
        task.setTitle("Before Update");
        task.setDescription("Desc Before");
        task.setTaskType(TaskType.LOW_PRIORITY);
        Task saved = taskRepository.save(task);

        saved.setTitle("After Update");
        saved.setDescription("Desc After");
        saved.setTaskType(TaskType.HIGH_PRIORITY);

        mockMvc.perform(put("/tasks/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("After Update"))
                .andExpect(jsonPath("$.description").value("Desc After"))
                .andExpect(jsonPath("$.taskType").value("HIGH_PRIORITY"));
    }

    @Test
    @DisplayName("Should delete a task")
    void testDeleteTask() throws Exception {
        Task task = new Task();
        task.setTitle("To Be Deleted");
        task.setDescription("Desc");
        task.setTaskType(TaskType.MEDIUM_PRIORITY);
        Task saved = taskRepository.save(task);

        mockMvc.perform(delete("/tasks/" + saved.getId()))
                .andExpect(status().isOk());

        Optional<Task> deleted = taskRepository.findById(saved.getId());
        assertTrue(deleted.isEmpty());
    }

    @Test
    @DisplayName("Should create a new task assigned to a project")
    void testCreateTaskWithProject() throws Exception {
        Project project = new Project();
        project.setName("My Project");
        Project savedProject = projectRepository.save(project);

        Task task = new Task();
        task.setTitle("Task with Project");
        task.setDescription("Description");
        task.setTaskType(TaskType.HIGH_PRIORITY);
        Project projectRef = new Project();
        projectRef.setId(savedProject.getId());
        task.setProject(projectRef);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task with Project"))
                .andExpect(jsonPath("$.project.id").value(savedProject.getId()));

        assertTrue(taskRepository.findAll().stream()
                .anyMatch(t -> t.getTitle().equals("Task with Project") &&
                        t.getProject() != null &&
                        t.getProject().getId().equals(savedProject.getId())));
    }
}
