package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Projects;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.Task_type;
import com.example.projectmanagerapp.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IntegrationExtraIT extends BaseIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Test
    void userCrudFlowIntegration() throws Exception {
        // CREATE USER
        var create = mvc.perform(post("/api/user")
                        .contentType(APPLICATION_JSON)
                        .content("{\"username\":\"John\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value("John"))
                .andReturn();
        User created = om.readValue(create.getResponse().getContentAsString(), User.class);

        // READ ALL
        var all = mvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andReturn();
        List<User> users = om.readValue(all.getResponse().getContentAsString(),
                om.getTypeFactory().constructCollectionType(List.class, User.class));
        assertThat(users).extracting("username").contains("John");

        // READ BY ID
        mvc.perform(get("/api/user/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John"));

        // UPDATE
        created.setUsername("Jane");
        mvc.perform(put("/api/user/{id}", created.getId())
                        .contentType(APPLICATION_JSON)
                        .content(om.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Jane"));

        // DELETE
        mvc.perform(delete("/api/user/{id}", created.getId()))
                .andExpect(status().isNoContent());

        // VERIFY DELETION
        mvc.perform(get("/api/user/{id}", created.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void taskProjectRelationshipFlow() throws Exception {
        // CREATE PROJECT
        var projResult = mvc.perform(post("/api/project")
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":\"RelProj\"}"))
                .andExpect(status().isCreated())
                .andReturn();
        Projects project = om.readValue(projResult.getResponse().getContentAsString(), Projects.class);

        // CREATE TASK linked to project
        String taskJson = String.format("{\"title\":\"LinkTask\",\"description\":\"Link Desc\",\"task_type\":\"BUG\",\"project\":{\"id\":%d}}", project.getId());
        var taskResult = mvc.perform(post("/api/task")
                        .contentType(APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("LinkTask"))
                .andReturn();
        Task task = om.readValue(taskResult.getResponse().getContentAsString(), Task.class);

        // VERIFY via project endpoint
        mvc.perform(get("/api/project/{id}", project.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasks[0].id").value(task.getId()));
    }
}
