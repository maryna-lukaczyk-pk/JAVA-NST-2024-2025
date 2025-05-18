package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

// Spring Boot + MockMvc
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

// Testcontainers
import org.testcontainers.junit.jupiter.Testcontainers;

// statyczne importy do MockMvc
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.jpa.hibernate.ddl-auto=create-drop"
)
@AutoConfigureMockMvc
@Testcontainers
public class TaskApiIT extends BaseIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Test
    void taskCrudFlow() throws Exception {
        // ... tutaj Twój istniejący CRUD‐flow dla Task ...
    }

    @Test
    void taskPriorityFieldFlow() throws Exception {
        // CREATE z priorityName = "High"
        var create = mvc.perform(post("/api/task")
                        .contentType(APPLICATION_JSON)
                        .content("""
                            {
                              "title":"Important task",
                              "description":"Desc",
                              "task_type":"FEATURE",
                              "priorityName":"High"
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.priorityName").value("High"))
                .andReturn();

        // Deserializuj obiekt i pobierz ID
        Task created = om.readValue(create.getResponse().getContentAsString(), Task.class);

        // READ BY ID → weryfikacja priorityName
        mvc.perform(get("/api/task/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priorityName").value("High"));
    }
}
