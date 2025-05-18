package org.example.projectmanagerapp.service.IntegrationTests;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        Users u1 = new Users();
        u1.setUsername("Krzysztof");

        Users u2 = new Users();
        u2.setUsername("Alicja");

        userRepository.saveAll(List.of(u1, u2));
    }

    @AfterEach
    void deleteTestRepo() {
        userRepository.deleteAll();
    }

    @Test
    public void shouldReturnAllUsers() throws Exception {
        mockMvc.perform(get("/api/users/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("Krzysztof")))
                .andExpect(jsonPath("$[1].username", is("Alicja")));
    }

    @Test
    public void shouldReturnUserById() throws Exception {
        Users user = new Users();
        user.setUsername("Krzysztof");
        user = userRepository.save(user);

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("User found: Krzysztof"));
    }

    //test metody create User
    @Test
    public void shouldCreateNewUser() throws Exception {
        Users newUser = new Users();
        newUser.setUsername("NowyUzytkownik");

        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("NowyUzytkownik")));
    }

    //test metody deleteUser
    @Test
    public void shouldDeleteUser() throws Exception {
        Users user = new Users();
        user.setUsername("DoUsuniecia");
        user = userRepository.save(user);

        mockMvc.perform(delete("/api/users/delete/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    //test metody updateUser
    @Test
    public void shouldUpdateExistingUser() throws Exception {
        Users user = new Users();
        user.setUsername("old_user");
        user = userRepository.save(user);

        Users updatedUser = new Users();
        updatedUser.setUsername("changed_user");

        mockMvc.perform(put("/api/users/update/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("User updated successfully"));
    }

    // teraz przypadki kiedy usuwamy albo pobieramy/updateujemy użytkowników, którzy nie istnieją
    // not found id

    @Test
    public void shouldReturnNotFoundWhenDeletingNonexistentUser() throws Exception {
        mockMvc.perform(delete("/api/users/delete/7861"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    public void shouldReturnNotFoundWhenUpdatingNonexistentUser() throws Exception {
        Users updatedUser = new Users();
        updatedUser.setUsername("Not_existing_user");

        mockMvc.perform(put("/api/users/update/7861")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with id: 7861 not found"));
    }

    @Test
    public void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/users/7861"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with id: 7861 not found"));
    }

}
