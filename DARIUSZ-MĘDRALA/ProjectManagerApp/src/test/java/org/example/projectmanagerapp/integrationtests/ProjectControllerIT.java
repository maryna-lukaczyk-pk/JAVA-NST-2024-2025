package org.example.projectmanagerapp.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
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


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ProjectControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
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
        Project project = new Project();
        project.setName("Test Project");
        projectRepository.save(project);

        Project project2 = new Project();
        project2.setName("Test Project 2");
        projectRepository.save(project2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Test Project"))
                .andExpect(jsonPath("$[1].name").value("Test Project 2"));
    }

    @Test
    void testCreateProject() throws Exception {
        Project project = new Project();
        project.setName("New Project");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/projects/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("New Project"));
    }

    @Test
    void testDeleteProject() throws Exception {
        Project project = new Project();
        project.setName("Project to Delete");
        project = projectRepository.save(project);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/projects/delete/" + project.getId()))
                .andExpect(status().isNoContent());

    }

    @Test
    void testUpdateProject() throws Exception {
        Project project = new Project();
        project.setName("Project to Update");
        project = projectRepository.save(project);

        Project updatedProject = new Project();
        updatedProject.setName("Updated Project");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/projects/update/" + project.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProject)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Updated Project"));
    }

    @Test
    void testGetProjectById() throws Exception {
        Project project = new Project();
        project.setName("Project to Get");
        project = projectRepository.save(project);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/" + project.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Project to Get"));
    }
}
