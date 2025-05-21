package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.TaskType;
import org.example.projectmanagerapp.priority.HighPriority;
import org.example.projectmanagerapp.priority.LowPriority;
import org.example.projectmanagerapp.priority.MediumPriority;
import org.junit.jupiter.api.BeforeEach;
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
class TaskControllerIntegrationIT {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test")
            .withUsername("sa")
            .withPassword("secret");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void cleanup() throws Exception {
        // clear all tasks
        mockMvc.perform(delete("/api/tasks/clear-all"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllTasks_empty_thenReturnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void createWithoutTypeOrPriority_defaultsApplied() throws Exception {
        String json = "{" +
                "\"title\":\"Write tests\"," +
                "\"description\":\"Cover all branches\"" +
                "}";

        String loc = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/tasks/")))
                .andReturn().getResponse().getHeader("Location");

        Long id = Long.valueOf(loc.substring(loc.lastIndexOf('/') + 1));

        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskType").value(TaskType.TODO.toString()))
                .andExpect(jsonPath("$.priority").value(new HighPriority().getPriority()));
    }

    @Test
    void createWithExplicitTypeAndPriority_usesGivenValues() throws Exception {
        String json = "{" +
                "\"title\":\"Custom task\"," +
                "\"description\":\"Custom desc\"," +
                "\"taskType\":\"IN_PROGRESS\"," +
                "\"priority\":\"" + new MediumPriority().getPriority() + "\"" +
                "}";

        String loc = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        Long id = Long.valueOf(loc.substring(loc.lastIndexOf('/') + 1));

        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskType").value(TaskType.IN_PROGRESS.toString()))
                .andExpect(jsonPath("$.priority").value(new MediumPriority().getPriority()));
    }

    @Test
    void createWithOnlyPriorityProvided_usesGivenPriorityAndDefaultType() throws Exception {
        String json = "{" +
                "\"title\":\"OnlyPriority\"," +
                "\"description\":\"desc\"," +
                "\"priority\":\"" + new LowPriority().getPriority() + "\"" +
                "}";

        String loc = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        Long id = Long.valueOf(loc.substring(loc.lastIndexOf('/') + 1));

        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskType").value(TaskType.TODO.toString()))
                .andExpect(jsonPath("$.priority").value(new LowPriority().getPriority()));
    }

    @Test
    void createWithOnlyTypeProvided_usesGivenTypeAndDefaultPriority() throws Exception {
        String json = "{" +
                "\"title\":\"OnlyType\"," +
                "\"description\":\"desc\"," +
                "\"taskType\":\"IN_PROGRESS\"" +
                "}";

        String loc = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        Long id = Long.valueOf(loc.substring(loc.lastIndexOf('/') + 1));

        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskType").value(TaskType.IN_PROGRESS.toString()))
                .andExpect(jsonPath("$.priority").value(new HighPriority().getPriority()));
    }

    @Test
    void update_onlyPriority_whenTaskTypeOmitted() throws Exception {
        // create
        String base = "{" +
                "\"title\":\"Base\"," +
                "\"description\":\"Desc\"" +
                "}";
        String loc = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(base))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        Long id = Long.valueOf(loc.substring(loc.lastIndexOf('/') + 1));

        // update priority only
        String upd = "{" +
                "\"title\":\"Updated\"," +
                "\"description\":\"Desc\"," +
                "\"priority\":\"" + new LowPriority().getPriority() + "\"" +
                "}";

        mockMvc.perform(put("/api/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(upd))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"))
                .andExpect(jsonPath("$.priority").value(new LowPriority().getPriority()))
                .andExpect(jsonPath("$.taskType").value(TaskType.TODO.toString()));
    }

    @Test
    void update_onlyTaskType_whenPriorityOmitted() throws Exception {
        // create
        String base = "{" +
                "\"title\":\"Base2\"," +
                "\"description\":\"Desc2\"" +
                "}";
        String loc = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(base))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        Long id = Long.valueOf(loc.substring(loc.lastIndexOf('/') + 1));

        // update taskType only
        String upd = "{" +
                "\"title\":\"Updated2\"," +
                "\"description\":\"Desc2\"," +
                "\"taskType\":\"DONE\"" +
                "}";

        mockMvc.perform(put("/api/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(upd))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskType").value(TaskType.DONE.toString()))
                .andExpect(jsonPath("$.priority").value(new HighPriority().getPriority()));
    }

    @Test
    void getAndDelete_nonExistentTasks() throws Exception {
        // GET missing
        mockMvc.perform(get("/api/tasks/9999"))
                .andExpect(status().isNotFound());
        // UPDATE missing
        mockMvc.perform(put("/api/tasks/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"X\",\"description\":\"Y\"}"))
                .andExpect(status().isNotFound());
        // DELETE missing
        mockMvc.perform(delete("/api/tasks/9999"))
                .andExpect(status().isOk());
    }

    @Test
    void listAllTasks_includesCreated() throws Exception {
        // create two
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"A\",\"description\":\"a\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"B\",\"description\":\"b\",\"priority\":\"MEDIUM\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }
}