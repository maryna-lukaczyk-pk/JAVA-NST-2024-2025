import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.projectmanagerapp.ProjectManagerAppApplication;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.TaskType;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(classes = ProjectManagerAppApplication.class)
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    private Project parentProject;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        parentProject = new Project();
        parentProject.setName("ParentProject");
        parentProject = projectRepository.save(parentProject);
    }

    @Test
    void testGetTasks() throws Exception {
        Task task1 = new Task();
        task1.setTitle("Task1");
        task1.setDescription("Desc1");
        task1.setTask_type(TaskType.FEATURE);
        task1.setProject(parentProject);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitle("Task2");
        task2.setDescription("Desc2");
        task2.setTask_type(TaskType.BUG);
        task2.setProject(parentProject);
        taskRepository.save(task2);

        mockMvc.perform(MockMvcRequestBuilders.get("/get-all-tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Task1"))
                .andExpect(jsonPath("$[1].title").value("Task2"));
    }

    @Test
    void testAddTask() throws Exception {
        Task task = new Task();
        task.setTitle("Task1");
        task.setDescription("Desc1");
        task.setTask_type(TaskType.FEATURE);
        task.setProject(parentProject);

        mockMvc.perform(MockMvcRequestBuilders.post("/create-task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Task1"));
    }

    @Test
    void testDeleteTask() throws Exception {
        Task task = new Task();
        task.setTitle("Task1");
        task.setDescription("Desc1");
        task.setTask_type(TaskType.FEATURE);
        task.setProject(parentProject);
        task = taskRepository.save(task);

        mockMvc.perform(MockMvcRequestBuilders.delete("/delete-task/" + task.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/get-all-tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testUpdateTask() throws Exception {
        Task task = new Task();
        task.setTitle("Task1");
        task.setDescription("Desc1");
        task.setTask_type(TaskType.FEATURE);
        task.setProject(parentProject);
        task = taskRepository.save(task);

        Task updatedTask = new Task();
        updatedTask.setTitle("Task2");
        updatedTask.setDescription("Desc2");
        updatedTask.setTask_type(TaskType.BUG);
        updatedTask.setProject(parentProject);

        mockMvc.perform(MockMvcRequestBuilders.put("/update-task/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Task2"));
    }

    @Test
    void testGetTaskById() throws Exception {
        Task task = new Task();
        task.setTitle("Task1");
        task.setDescription("Desc1");
        task.setTask_type(TaskType.FEATURE);
        task.setProject(parentProject);
        task = taskRepository.save(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/get-task/" + task.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Task1"));
    }
}
