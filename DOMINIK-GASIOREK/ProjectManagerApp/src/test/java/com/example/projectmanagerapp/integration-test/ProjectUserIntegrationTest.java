import com.example.projectmanagerapp.ProjectManagerAppApplication;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.ProjectUser;
import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.ProjectUserRepository;
import com.example.projectmanagerapp.repository.UserRepository;
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

@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(classes = ProjectManagerAppApplication.class)
public class ProjectUserIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProjectUserRepository projectUserRepository;

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

    private Project project;
    private User user;

    @BeforeEach
    void setUp() {
        projectUserRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        project = new Project();
        project.setName("ProjectA");
        project = projectRepository.save(project);

        user = new User();
        user.setUsername("UserA");
        user = userRepository.save(user);
    }

    @Test
    void testAddUserToProject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/add-user-to-project")
                        .param("userId", user.getId().toString())
                        .param("projectId", project.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user.id").value(user.getId()))
                .andExpect(jsonPath("$.project.id").value(project.getId()));
    }
}
