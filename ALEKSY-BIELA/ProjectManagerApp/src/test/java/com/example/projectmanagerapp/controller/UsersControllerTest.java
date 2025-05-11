package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class UsersControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    UsersControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    @DisplayName("getUserById")
    void getUserById() throws Exception {
        Users users = new Users();
        users.setUsername("Edward");
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(users)))
                .andExpect(jsonPath("$.username", is("Edward")))
                .andExpect(status().is2xxSuccessful()).andReturn();

        mockMvc.perform(get("/api/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("Edward")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(status().is2xxSuccessful()).andReturn();
    }

    @Test
    @DisplayName("createUser")
    void createUser() throws Exception {
        Users users = new Users();
        users.setUsername("Edward");
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(users)))
                .andExpect(jsonPath("$.username", is("Edward")))
                .andExpect(status().is2xxSuccessful()).andReturn();

    }

    @Test
    @DisplayName("updateUser")
    void updateUser() throws Exception {
        Users users = new Users();
        users.setUsername("Edward");
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(users)))
                .andExpect(jsonPath("$.username", is("Edward")))
                .andExpect(status().is2xxSuccessful()).andReturn();

        Users userUpdate = new Users();
        userUpdate.setUsername("Gierek");
        mockMvc.perform(put("/api/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate)))
                .andExpect(jsonPath("$.username", is("Gierek")))
                .andExpect(status().is2xxSuccessful()).andReturn();

    }

    @Test
    @DisplayName("deleteUser")
    void deleteUser() throws Exception {
        Users users = new Users();
        users.setUsername("Edward");
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(users)))
                .andExpect(jsonPath("$.username", is("Edward")))
                .andExpect(status().is2xxSuccessful()).andReturn();

        mockMvc.perform(delete("/api/users/{id}",1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();

        mockMvc.perform(get("/api/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
    }
}
