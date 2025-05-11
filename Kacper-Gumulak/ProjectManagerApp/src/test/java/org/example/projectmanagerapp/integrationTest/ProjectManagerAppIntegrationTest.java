package org.example.projectmanagerapp.integrationTest;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

// Klasa testowa do integracji aplikacji ProjectManagerApp
@SpringBootTest
@AutoConfigureMockMvc
public class ProjectManagerAppIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private static PostgreSQLContainer<?> postgresContainer;

    private Long userId;
    private Long projectId;

    @BeforeAll
    public static void setUp() {
        postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                        .withDatabaseName("task_manager")
                        .withUsername("postgres")
                        .withPassword("kapi2000");
        postgresContainer.start();
    }

    @AfterAll
    static void tearDown() {
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

    @Test
    public void testAssignUserToProject() throws Exception {
        if (userId == null || projectId == null) {
            throw new IllegalStateException("User ID and Project ID must be set before assigning.");
        }

        mockMvc.perform(post("/users/" + userId + "/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.user_id").value(userId))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.project_id").value(projectId));
    }
}