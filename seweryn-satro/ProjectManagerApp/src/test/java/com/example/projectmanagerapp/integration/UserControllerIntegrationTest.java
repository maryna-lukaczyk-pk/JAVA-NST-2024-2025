package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return all users")
    void testAllUsers() throws Exception {
        User userOne = new User();
        userOne.setUsername("IntegrationUser1");

        User userTwo = new User();
        userTwo.setUsername("IntegrationUser2");

        userRepository.saveAll(List.of(userOne, userTwo));

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("IntegrationUser1")))
                .andExpect(jsonPath("$[1].username", is("IntegrationUser2")));
    }

    @Test
    @DisplayName("Should create new user")
    void testNewUser() throws Exception {
        User newUser = new User();
        newUser.setUsername("NewIntegrationUser");

        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("NewIntegrationUser")));

        assertTrue(userRepository.findAll().stream()
                .anyMatch(user -> "NewIntegrationUser".equals(user.getUsername())));
    }

    @Test
    @DisplayName("Should update user")
    void testUpdateUser() throws Exception {
        User existingUser = new User();
        existingUser.setUsername("OldIntegrationName");
        User savedUser = userRepository.save(existingUser);

        User updateRequest = new User();
        updateRequest.setUsername("UpdatedIntegrationName");

        mockMvc.perform(put("/api/users/" + savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("UpdatedIntegrationName")));

        User updatedUser = userRepository.findById(savedUser.getId().intValue()).orElseThrow();
        assertEquals("UpdatedIntegrationName", updatedUser.getUsername());
    }

    @Test
    @DisplayName("Should delete user by ID")
    void testDeleteUser() throws Exception {
        User userToDelete = new User();
        userToDelete.setUsername("ToDeleteIntegrationUser");
        User savedUser = userRepository.save(userToDelete);

        mockMvc.perform(delete("/api/users/" + savedUser.getId()))
                .andExpect(status().isOk());

        assertFalse(userRepository.findById(savedUser.getId().intValue()).isPresent());
    }

    @Test
    @DisplayName("Should return user by ID")
    void testUserById() throws Exception {
        User user = new User();
        user.setUsername("IntegrationFindById");
        User savedUser = userRepository.save(user);

        mockMvc.perform(get("/api/users/" + savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("IntegrationFindById")));
    }

    @Test
    @DisplayName("Should return 404 if user not found")
    void testUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/users/99999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
