package org.example.projectmanagerapp.tests.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // Usuń wszystkich użytkowników przez natywne zapytanie SQL
        userRepository.deleteAllInBatch();
    }

    @Test
    public void createUser_ShouldReturn201() throws Exception {
        User user = new User();
        user.setUsername("testuser");

        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    public void getUserById_ShouldReturnUser() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user = userRepository.save(user);

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }
    @Test
    public void getUsers_ShouldReturnAllUsers() throws Exception {
        User user1 = new User();
        user1.setUsername("user1");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        userRepository.save(user2);

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
    @Test
    public void deleteUser_ShouldReturn204() throws Exception {
        User user = new User();
        user.setUsername("To Delete");
        user = userRepository.save(user);

        mockMvc.perform(delete("/api/users/delete/" + user.getId()))
                .andExpect(status().isNoContent());
    }
}