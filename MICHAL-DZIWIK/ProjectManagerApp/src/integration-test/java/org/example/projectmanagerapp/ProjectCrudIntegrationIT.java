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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProjectCrudIntegrationIT {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("test")
                    .withUsername("sa")
                    .withPassword("secret");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    MockMvc mockMvc;

    @Test
    void fullCrudProjectBranches() throws Exception {
        // GET ALL — initially empty
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        // GET non-existing by ID => 404
        mockMvc.perform(get("/api/projects/999"))
                .andExpect(status().isNotFound());

        // UPDATE non-existing => 404
        String upMissing = "{\"name\":\"ghost\"}";
        mockMvc.perform(put("/api/projects/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(upMissing))
                .andExpect(status().isNotFound());

        // DELETE non-existing => still 200 (controller always returns ok)
        mockMvc.perform(delete("/api/projects/999"))
                .andExpect(status().isOk());

        // CREATE
        String prJson = "{\"name\":\"alpha\"}";
        String loc = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(prJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/projects/")))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        Long id = extractId(loc);

        // GET ALL after create
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("alpha"));

        // GET by ID — found
        mockMvc.perform(get("/api/projects/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("alpha"));

        // UPDATE existing
        String upJson = "{\"name\":\"beta\"}";
        mockMvc.perform(put("/api/projects/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(upJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("beta"));

        // GET ALL after update
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("beta"));

        // DELETE existing
        mockMvc.perform(delete("/api/projects/" + id))
                .andExpect(status().isOk());

        // VERIFY deleted by ID => 404
        mockMvc.perform(get("/api/projects/" + id))
                .andExpect(status().isNotFound());

        // GET ALL back to empty
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    private Long extractId(String location) {
        String[] parts = location.split("/");
        return Long.valueOf(parts[parts.length - 1]);
    }
}
