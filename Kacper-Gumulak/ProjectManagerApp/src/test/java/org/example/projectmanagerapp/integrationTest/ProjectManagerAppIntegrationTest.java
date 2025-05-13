package org.example.projectmanagerapp.integrationTest;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Klasa testowa do integracji aplikacji ProjectManagerApp
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProjectManagerAppIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private static PostgreSQLContainer<?> postgresContainer;

    private Long userId;
    private Long projectId;
    private Long taskId;

    @BeforeAll
    public static void setUp() {
        postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                        .withDatabaseName("task_manager")
                        .withUsername("postgres")
                        .withPassword("kapi2000");
        postgresContainer.start();
    }

    @AfterAll
    public void tearDown() throws Exception {
        if (taskId != null) { mockMvc.perform(delete("/tasks/" + taskId)).andExpect(MockMvcResultMatchers.status().isOk()); }

        if (projectId != null) { mockMvc.perform(delete("/projects/" + projectId)).andExpect(MockMvcResultMatchers.status().isOk()); }

        if (userId != null) { mockMvc.perform(delete("/users/" + userId)).andExpect(MockMvcResultMatchers.status().isOk()); }

        if (postgresContainer != null) { postgresContainer.stop(); }
    }

    @BeforeEach
    public void createUserAndProject() throws Exception {
        String userJson = "{\"username\":\"testUser\", \"project_id\":[] }";
        String userResponse = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("testUser"))
                        .andReturn().getResponse().getContentAsString();

        userId = Long.valueOf(JsonPath.read(userResponse, "$.id").toString());

        String projectJson = "{\"name\":\"testProject\", \"user_id\":[], \"task_id\":[] }";
        String projectResponse = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("testProject"))
                        .andReturn().getResponse().getContentAsString();
        projectId = Long.valueOf(JsonPath.read(projectResponse, "$.id").toString());
    }

    @AfterEach
    public void cleanupEach() throws Exception {
        if (taskId != null) {
            mockMvc.perform(delete("/tasks/" + taskId)).andExpect(status().isOk());
            taskId = null;
        }
        if (projectId != null) {
            mockMvc.perform(delete("/projects/" + projectId)).andExpect(status().isOk());
            projectId = null;
        }
        if (userId != null) {
            mockMvc.perform(delete("/users/" + userId)).andExpect(status().isOk());
            userId = null;
        }
    }

    @Test
    public void testAssignUserToProject() throws Exception {
        mockMvc.perform(post("/users/" + userId + "/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.project_id[0]").value(projectId));
    }

    @Test
    public void testCreateTaskForProject() throws Exception {
        String taskJson = """
            {
              "title":"testTask",
              "description":"testDescription",
              "taskType":"FEATURE",
              "project":{"id":%d},
              "priority":"HIGH"
            }
            """.formatted(projectId);

        String taskResponse = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("testTask"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("testDescription"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.task_type").value("FEATURE"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.project_id").value(projectId))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("HIGH"))
                        .andReturn().getResponse().getContentAsString();

        taskId = Long.valueOf(JsonPath.read(taskResponse, "$.id").toString());
        mockMvc.perform(get("/tasks")).andExpect(MockMvcResultMatchers.jsonPath("$.[?(@.id == %d)]", taskId).exists());
    }
}