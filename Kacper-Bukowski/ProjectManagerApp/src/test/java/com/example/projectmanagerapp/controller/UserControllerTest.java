package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import com.example.projectmanagerapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setUsername("TestUser1");

        user2 = new User();
        user2.setUsername("TestUser2");
    }

    @Test
    @DisplayName("Should return all users")
    void getAllUsers() throws Exception {
        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("TestUser1")))
                .andExpect(jsonPath("$[1].username", is("TestUser2")));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Should return user by ID")
    void getUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(user1);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("TestUser1")));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("Should create a new user")
    void createUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user1);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("TestUser1")));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    @DisplayName("Should update user")
    void updateUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setUsername("UpdatedUser");

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("UpdatedUser")));

        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    @Test
    @DisplayName("Should delete user")
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }
}
