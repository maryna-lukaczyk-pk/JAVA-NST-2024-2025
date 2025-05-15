package org.example.projectmanagerapp;

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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskCrudIntegrationIT {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("test")
                    .withUsername("sa")
                    .withPassword("secret");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url",      postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    MockMvc mockMvc;

    @Test
    void createWithoutTypeOrPriority_DefaultsAreApplied() throws Exception {
        String json = """
            {
              "title":"Write tests",
              "description":"Cover all branches"
            }
            """;

        String loc = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/tasks/")))
                .andReturn().getResponse().getHeader("Location");

        Long id = extractId(loc);

        // GET back and assert defaults: TODO & HIGH
        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Write tests"))
                .andExpect(jsonPath("$.taskType").value("TODO"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    @Test
    void createWithExplicitTypeAndPriority_UsesGivenValues() throws Exception {
        String json = """
            {
              "title":"Custom task",
              "description":"Custom desc",
              "taskType":"IN_PROGRESS",
              "priority":"MEDIUM"
            }
            """;

        String loc = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        Long id = extractId(loc);

        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskType").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"));
    }

    @Test
    void updateBranches_changeTitleAndPriority_onlyIfNotNull() throws Exception {
        // first create one
        String base = """
            {
              "title":"Base",
              "description":"Desc"
            }
            """;
        String loc = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(base))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        Long id = extractId(loc);

        // now update: supply a new title + priority but omit taskType
        String upd = """
            {
              "title":"Updated",
              "description":"Desc",
              "priority":"LOW"
            }
            """;

        mockMvc.perform(put("/api/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(upd))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"))
                .andExpect(jsonPath("$.priority").value("LOW"))
                // since we omitted taskType, it stays the default
                .andExpect(jsonPath("$.taskType").value("TODO"));
    }

    @Test
    void notFound_get_update_delete_branches() throws Exception {
        // GET nonexistent
        mockMvc.perform(get("/api/tasks/9999"))
                .andExpect(status().isNotFound());

        // UPDATE nonexistent
        String some = """
            {
              "title":"X","description":"Y"
            }
            """;
        mockMvc.perform(put("/api/tasks/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(some))
                .andExpect(status().isNotFound());

        // DELETE nonexistent → always 200 per spec
        mockMvc.perform(delete("/api/tasks/9999"))
                .andExpect(status().isOk());
    }

    @Test
    void listAllTasks_includesCreatedOnes() throws Exception {
        // create two
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                  {"title":"A","description":"a"}
                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                  {"title":"B","description":"b","priority":"MEDIUM"}
                """))
                .andExpect(status().isCreated());

        // list and expect at least 2 entries
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    private Long extractId(String location) {
        String[] parts = location.split("/");
        return Long.valueOf(parts[parts.length - 1]);
    }
}
