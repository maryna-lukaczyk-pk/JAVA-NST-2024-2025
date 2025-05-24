package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.TaskType;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.repository.ProjectRepository;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TaskControllerIT {

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
            .withDatabaseName("test-database")
            .withUsername("test")
            .withPassword("test123");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    @DisplayName("Should create a new task")
    void testCreateTask() throws Exception {
        Tasks task = new Tasks();
        task.setTitle("NewTask");
        task.setDescription("Description");
        task.setTaskType(TaskType.HIGH_PRIORITY);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("NewTask"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.taskType").value("HIGH_PRIORITY"));

        assertTrue(taskRepository.findAll().stream().anyMatch(t -> t.getTitle().equals("NewTask")));
    }

    @Test
    @DisplayName("Should get all tasks")
    void testGetAllTasks() throws Exception {
        taskRepository.deleteAll();

        Tasks task1 = new Tasks();
        task1.setTitle("Task1");
        task1.setDescription("Desc1");
        task1.setTaskType(TaskType.LOW_PRIORITY);
        taskRepository.save(task1);

        Tasks task2 = new Tasks();
        task2.setTitle("Task2");
        task2.setDescription("Desc2");
        task2.setTaskType(TaskType.MEDIUM_PRIORITY);
        taskRepository.save(task2);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Should get task by ID")
    void testGetTaskById() throws Exception {
        Tasks task = new Tasks();
        task.setTitle("FindMe123");
        task.setDescription("Desc");
        task.setTaskType(TaskType.MEDIUM_PRIORITY);
        Tasks saved = taskRepository.save(task);

        mockMvc.perform(get("/tasks/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("FindMe123"));
    }

    @Test
    @DisplayName("Should update existing task")
    void testUpdateTask() throws Exception {
        Tasks task = new Tasks();
        task.setTitle("Before Update");
        task.setDescription("Desc Before");
        task.setTaskType(TaskType.LOW_PRIORITY);
        Tasks saved = taskRepository.save(task);

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
        Tasks task = new Tasks();
        task.setTitle("ToBeRemoved");
        task.setDescription("Desc");
        task.setTaskType(TaskType.MEDIUM_PRIORITY);
        Tasks saved = taskRepository.save(task);

        mockMvc.perform(delete("/tasks/" + saved.getId()))
                .andExpect(status().isOk());

        Optional<Tasks> deleted = taskRepository.findById(saved.getId());
        assertTrue(deleted.isEmpty());
    }

    @Test
    @DisplayName("Should create a new task assigned to a project")
    void testCreateTaskWithProject() throws Exception {
        Project project = new Project();
        project.setName("MyProject");
        Project savedProject = projectRepository.save(project);

        Tasks task = new Tasks();
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