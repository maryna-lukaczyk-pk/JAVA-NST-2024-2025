package org.example.projectmanagerapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;
    private User testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
    }

    @Test
    @DisplayName("It should get user by ID")
    void getUser() throws Exception {
        when(userService.getUserById(1L)).thenReturn(testUser);

        mockMvc.perform(get("/api/v1/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("It should return 404 when user not found")
    void getUserNotFound() throws Exception {
        when(userService.getUserById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/user/999"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(999L);
    }

    @Test
    @DisplayName("It should get all users")
    void getAllUsers() throws Exception {
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        List<User> users = Arrays.asList(testUser, user2);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/v1/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("user2"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("It should create a new user")
    void createUser() throws Exception {
        User newUser = new User();
        newUser.setUsername("newuser");

        User savedUser = new User();
        savedUser.setId(3L);
        savedUser.setUsername("newuser");

        when(userService.createUser(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.username").value("newuser"));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    @DisplayName("It should update an existing user")
    void updateUser() throws Exception {
        User userDetails = new User();
        userDetails.setUsername("updateduser");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updateduser");

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/v1/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("updateduser"));

        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    @Test
    @DisplayName("It should return 404 when updating non-existent user")
    void updateUserNotFound() throws Exception {
        User userDetails = new User();
        userDetails.setUsername("updateduser");

        when(userService.updateUser(eq(999L), any(User.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Użytkownik o ID 999 nie został znaleziony"));

        mockMvc.perform(put("/api/v1/user/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDetails)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).updateUser(eq(999L), any(User.class));
    }

    @Test
    @DisplayName("It should delete a user")
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/v1/user/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    @DisplayName("It should return 404 when deleting non-existent user")
    void deleteUserNotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Użytkownik o ID 999 nie został znaleziony"))
                .when(userService).deleteUser(999L);

        mockMvc.perform(delete("/api/v1/user/999"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).deleteUser(999L);
    }

    @Test
    @DisplayName("It should assign user to project")
    void assignUserToProject() throws Exception {
        Long projectId = 2L;
        doNothing().when(userService).assignUserToProject(1L, projectId);

        mockMvc.perform(post("/api/v1/user/1/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectId)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).assignUserToProject(1L, projectId);
    }

    @Test
    @DisplayName("It should throw exception when assigning to non-existent project")
    void assignUserToProjectNotFound() throws Exception {
        Long projectId = 999L;
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Projekt o ID 999 nie został znaleziony"))
                .when(userService).assignUserToProject(1L, projectId);

        mockMvc.perform(post("/api/v1/user/1/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectId)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).assignUserToProject(1L, projectId);
    }

    @Test
    @DisplayName("It should throw exception when assigning non-existent user")
    void assignNonExistentUserToProject() throws Exception {
        Long projectId = 2L;
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Użytkownik o ID 999 nie został znaleziony"))
                .when(userService).assignUserToProject(999L, projectId);

        mockMvc.perform(post("/api/v1/user/999/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectId)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).assignUserToProject(999L, projectId);
    }

    @Test
    @DisplayName("It should remove user from project")
    void removeUserFromProject() throws Exception {
        doNothing().when(userService).removeUserFromProject(1L, 2L);

        mockMvc.perform(delete("/api/v1/user/1/project/2"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).removeUserFromProject(1L, 2L);
    }

    @Test
    @DisplayName("It should throw exception when removing non-existent user from project")
    void removeNonExistentUserFromProject() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Użytkownik o ID 999 nie został znaleziony"))
                .when(userService).removeUserFromProject(999L, 2L);

        mockMvc.perform(delete("/api/v1/user/999/project/2"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).removeUserFromProject(999L, 2L);
    }

    @Test
    @DisplayName("It should throw exception when removing user from non-existent project")
    void removeUserFromNonExistentProject() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Projekt o ID 999 nie został znaleziony"))
                .when(userService).removeUserFromProject(1L, 999L);

        mockMvc.perform(delete("/api/v1/user/1/project/999"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).removeUserFromProject(1L, 999L);
    }
}