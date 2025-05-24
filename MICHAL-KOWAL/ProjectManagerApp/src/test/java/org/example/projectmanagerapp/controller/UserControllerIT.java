package org.example.projectmanagerapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser1 = new User();
        testUser1.setUsername("Darek");

        testUser2 = new User();
        testUser2.setUsername("Jerzy");

        userRepository.saveAll(List.of(testUser1, testUser2));
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is(testUser1.getUsername())))
                .andExpect(jsonPath("$[1].username", is(testUser2.getUsername())));
    }

    @Test
    void getUserById_ReturnsUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", testUser1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(testUser1.getUsername())));
    }

    @Test
    void getUserById_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_ReturnsCreatedUser() throws Exception {
        User newUser = new User();
        newUser.setUsername("newuser");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(newUser.getUsername())));
    }

    @Test
    void updateUser_ReturnsUpdatedUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setUsername("updateduser");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/update/{id}", testUser1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(updatedUser.getUsername())));
    }

    @Test
    void updateUser_NotFound() throws Exception {
        User updatedUser = new User();
        updatedUser.setUsername("updateduser");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/update/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_ReturnsOkStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/delete/{id}", testUser1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", testUser1.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/delete/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}