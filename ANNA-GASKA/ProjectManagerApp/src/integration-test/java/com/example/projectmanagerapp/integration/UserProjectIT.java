package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.entity.Projects;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserProjectIT extends BaseIT {
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;

    @Test
    void assignUserToProjectFlow() throws Exception {
        // CREATE USER
        var userResult = mvc.perform(post("/api/user")
                        .contentType(APPLICATION_JSON)
                        .content("{\"username\":\"Anna\"}"))
                .andExpect(status().isCreated())
                .andReturn();
        User user = om.readValue(userResult.getResponse().getContentAsString(), User.class);

        // CREATE PROJECT
        var projResult = mvc.perform(post("/api/project")
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":\"ProjX\"}"))
                .andExpect(status().isCreated())
                .andReturn();
        Projects project = om.readValue(projResult.getResponse().getContentAsString(), Projects.class);

        // ASSIGN
        mvc.perform(post("/api/project/" + project.getId() + "/users")
                        .param("userId", user.getId().toString()))
                .andExpect(status().isOk());

        // VERIFY RELATION
        mvc.perform(get("/api/project/{id}", project.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].id").value(user.getId()));
    }
}