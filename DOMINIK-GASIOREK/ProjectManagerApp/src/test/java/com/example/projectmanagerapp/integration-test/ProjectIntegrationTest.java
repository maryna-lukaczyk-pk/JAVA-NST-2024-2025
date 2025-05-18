import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.ProjectManagerAppApplication;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;


@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(classes = ProjectManagerAppApplication.class)
public class ProjectIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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

    @BeforeEach
    void setUp() {
        projectRepository.deleteAll();
    }

    @Test
    void testGetProjects() throws Exception {
        Project project1 = new Project();
        project1.setName("TestProject1");
        projectRepository.save(project1);

        Project project2 = new Project();
        project2.setName("TestProject2");
        projectRepository.save(project2);

        mockMvc.perform(MockMvcRequestBuilders.get("/get-all-projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("TestProject1"))
                .andExpect(jsonPath("$[1].name").value("TestProject2"));
    }

    @Test
    void testAddProject() throws Exception {
        Project project1 = new Project();
        project1.setName("TestProject1");

        mockMvc.perform(MockMvcRequestBuilders.post("/create-project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("TestProject1"));
    }

    @Test
    void testDeleteProject() throws Exception {
        Project project1 = new Project();
        project1.setName("TestProject1");
        project1 = projectRepository.save(project1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/delete-project/" + project1.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/get-all-projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testUpdateProject() throws Exception {
        Project project1 = new Project();
        project1.setName("TestProject1");
        project1 = projectRepository.save(project1);

        Project project2 = new Project();
        project2.setName("TestProject2");

        mockMvc.perform(MockMvcRequestBuilders.put("/update-project/" + project1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("TestProject2"));
    }

    @Test
    void testGetProjectById() throws Exception {
        Project project1 = new Project();
        project1.setName("TestProject1");
        project1 = projectRepository.save(project1);

        mockMvc.perform(MockMvcRequestBuilders.get("/get-project/" + project1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("TestProject1"));
    }
}