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

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @Test
    void assignUserToProjectFlow() throws Exception {
        // a) CREATE USER
        String userJson = "{\"username\":\"Anna\"}";
        var userResult = mvc.perform(post("/api/user")
                        .contentType(APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andReturn();
        User u = om.readValue(userResult.getResponse().getContentAsString(), User.class);

        // b) CREATE PROJECT
        String projJson = "{\"name\":\"ProjX\"}";
        var projResult = mvc.perform(post("/api/project")
                        .contentType(APPLICATION_JSON)
                        .content(projJson))
                .andExpect(status().isCreated())
                .andReturn();
        Projects p = om.readValue(projResult.getResponse().getContentAsString(), Projects.class);

        // c) ASSIGN user to project
        mvc.perform(post("/api/project/" + p.getId() + "/users")
                        .param("userId", u.getId().toString()))
                .andExpect(status().isOk());

        // d) VERIFY relation
        mvc.perform(get("/api/project/" + p.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].id").value(u.getId()));
    }
}
