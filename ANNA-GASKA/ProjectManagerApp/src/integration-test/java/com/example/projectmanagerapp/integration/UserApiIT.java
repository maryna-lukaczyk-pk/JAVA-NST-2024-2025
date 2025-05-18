package com.example.projectmanagerapp.integration;

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

public class UserApiIT extends BaseIT {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Test
    void userCrudFlow() throws Exception {
        // CREATE
        var createResult = mvc.perform(post("/api/user")
                        .contentType(APPLICATION_JSON)
                        .content("{\"username\":\"alice\"}"))
                .andExpect(status().isCreated())
                .andReturn();
        User created = om.readValue(createResult.getResponse().getContentAsString(), User.class);

        // READ ALL
        var allResult = mvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andReturn();
        List<User> users = om.readValue(allResult.getResponse().getContentAsString(),
                om.getTypeFactory().constructCollectionType(List.class, User.class));
        assertThat(users).extracting("username").contains("alice");

        // READ BY ID
        mvc.perform(get("/api/user/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice"));

        // UPDATE
        created.setUsername("alice2");
        mvc.perform(put("/api/user/{id}", created.getId())
                        .contentType(APPLICATION_JSON)
                        .content(om.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice2"));

        // DELETE
        mvc.perform(delete("/api/user/{id}", created.getId()))
                .andExpect(status().isNoContent());

        // VERIFY DELETION → 404 Not Found
        mvc.perform(get("/api/user/{id}", created.getId()))
                .andExpect(status().isNotFound());
    }
}

