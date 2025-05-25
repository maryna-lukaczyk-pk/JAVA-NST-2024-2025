package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.entity.Tasks;
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

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class TaskIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldPerformFullCrudForTask() throws Exception {

        Projects project = new Projects();
        project.setName("Test Project");

        String projectJson = objectMapper.writeValueAsString(project);

        String projectResponse = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long projectId = objectMapper.readTree(projectResponse).get("id").asLong();


        Projects taskProject = new Projects();
        taskProject.setId(projectId);

        Tasks task = new Tasks();
        task.setTitle("Test Task");
        task.setDescription("Testing task creation");
        task.setProjects(taskProject);

        String taskJson = objectMapper.writeValueAsString(task);

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andReturn().getResponse().getContentAsString();

        Long taskId = objectMapper.readTree(response).get("id").asLong();


        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId));


        task.setTitle("Updated Task");
        String updatedJson = objectMapper.writeValueAsString(task);

        mockMvc.perform(put("/api/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));


        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasItem(taskId.intValue())));


        mockMvc.perform(delete("/api/tasks/" + taskId))
                .andExpect(status().isOk());
    }
}
