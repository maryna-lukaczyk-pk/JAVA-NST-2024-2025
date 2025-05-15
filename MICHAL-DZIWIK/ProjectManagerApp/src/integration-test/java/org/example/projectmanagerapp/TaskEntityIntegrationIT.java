package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.TaskType;
import org.example.projectmanagerapp.priority.HighPriority;
import org.example.projectmanagerapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
public class TaskEntityIntegrationIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test")
            .withUsername("sa")
            .withPassword("secret");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // ensure a clean state
        taskService.getAllTasks().forEach(t -> taskService.deleteTask(t.getId()));
    }

    @Test
    void createTask_withNullTypeAndPriority_appliesDefaults() {
        Task task = new Task();
        task.setTitle("Title");
        task.setDescription("Description");
        // explicitly pass null to trigger default logic in setters
        task.setTaskType(null);
        task.setPriority(null);

        Task saved = taskService.createTask(task);
        assertNotNull(saved.getId(), "Saved task should have an ID");
        assertEquals(TaskType.TODO, saved.getTaskType(), "Null taskType should default to TODO");
        assertEquals(new HighPriority().getPriority(), saved.getPriority(), "Null priority should default to HIGH");
    }

    @Test
    void updateTask_withNullTypeAndPriority_preservesDefaults() {

        Task initial = new Task();
        initial.setTitle("Init");
        initial.setDescription("Desc");
        initial.setTaskType(TaskType.IN_PROGRESS);
        initial.setPriority(new HighPriority().getPriority());
        Task created = taskService.createTask(initial);

        Task update = new Task();
        update.setTitle(created.getTitle());
        update.setDescription(created.getDescription());
        update.setTaskType(null);
        update.setPriority(null);

        Task updated = taskService.updateTask(created.getId(), update);
        assertEquals(TaskType.TODO, updated.getTaskType(), "Null on update should reset to TODO");
        assertEquals(new HighPriority().getPriority(), updated.getPriority(), "Null on update should reset to HIGH");
    }
}
