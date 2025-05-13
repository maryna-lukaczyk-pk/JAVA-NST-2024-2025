package org.example.projectmanagerapp.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.*;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.TaskType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class TaskControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("test");
    @Autowired
    private ProjectRepository projectRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void testGetTasks() throws Exception {

        Project project = new Project();
        project.setName("Test Project");
        projectRepository.save(project);

        Task task1 = new Task();
        task1.setTitle("Test Task");
        task1.setDescription("Test Description");
        task1.setTaskType(TaskType.FEATURE);
        task1.setProject(project);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitle("Another Task");
        task2.setDescription("Test Description");
        task2.setTaskType(TaskType.BUGFIX);
        task2.setProject(project);
        taskRepository.save(task2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/task/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[1].title").value("Another Task"));
    }

    @Test
    void testCreateTask() throws Exception {

        Project project = new Project();
        project.setName("Test Project for Creation");
        projectRepository.save(project);

        Task newTask = new Task();
        newTask.setTitle("Test Task to Create");
        newTask.setDescription("Description for creation");
        newTask.setTaskType(TaskType.FEATURE);
        newTask.setProject(project);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Task to Create"));
    }

    @Test
    void testDeleteTask() throws Exception {

        Project project = new Project();
        project.setName("Test Project for deletion");
        projectRepository.save(project);

        Task newTask = new Task();
        newTask.setTitle("Test Task to Delete");
        newTask.setDescription("Description for deletion");
        newTask.setTaskType(TaskType.FEATURE);
        newTask.setProject(project);
        taskRepository.save(newTask);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/task/delete/" + newTask.getId()))
                .andExpect(status().isNoContent());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/task/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testUpdateTask() throws Exception {

        Project project = new Project();
        project.setName("Test Project for update");
        projectRepository.save(project);

        Task newTask = new Task();
        newTask.setTitle("Test Task to Update");
        newTask.setDescription("Description for update");
        newTask.setTaskType(TaskType.FEATURE);
        newTask.setProject(project);
        taskRepository.save(newTask);

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setTaskType(TaskType.BUGFIX);
        updatedTask.setProject(project);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/task/update/" + newTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }


    @Test
    void testGetTaskById() throws Exception {

        Project project = new Project();
        project.setName("Test Project for retrieval");
        projectRepository.save(project);

        Task newTask = new Task();
        newTask.setTitle("Test Task to Retrieve");
        newTask.setDescription("Description for retrieval");
        newTask.setTaskType(TaskType.FEATURE);
        newTask.setProject(project);
        taskRepository.save(newTask);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/task/" + newTask.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Task to Retrieve"));
    }
}
