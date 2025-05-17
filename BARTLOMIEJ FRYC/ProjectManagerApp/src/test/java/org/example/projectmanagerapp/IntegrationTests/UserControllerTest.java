package org.example.projectmanagerapp.IntegrationTests;

import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.example.projectmanagerapp.controller.UserController;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("Should return all users")
    void testGetAllUsers() throws Exception {
        // Arrange
        Users user1 = new Users();
        user1.setUser_id(1L);
        user1.setUser_name("TestUser1");

        Users user2 = new Users();
        user2.setUser_id(2L);
        user2.setUser_name("TestUser2");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        // Act & Assert
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user_id").value(1))
                .andExpect(jsonPath("$[0].user_name").value("TestUser1"))
                .andExpect(jsonPath("$[1].user_id").value(2))
                .andExpect(jsonPath("$[1].user_name").value("TestUser2"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Should return user by ID when user exists")
    void testGetUserByIdWhenUserExists() throws Exception {
        // Arrange
        Users user = new Users();
        user.setUser_id(1L);
        user.setUser_name("TestUser");

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        // Act & Assert
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(1))
                .andExpect(jsonPath("$.user_name").value("TestUser"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("Should return 404 when user does not exist")
    void testGetUserByIdWhenUserDoesNotExist() throws Exception {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("Should create a new user")
    void testCreateUser() throws Exception {
        // Arrange
        Users user = new Users();
        user.setUser_name("NewUser");

        Users savedUser = new Users();
        savedUser.setUser_id(1L);
        savedUser.setUser_name("NewUser");

        when(userService.createUser(any(Users.class))).thenReturn(savedUser);

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"user_name\":\"NewUser\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(1))
                .andExpect(jsonPath("$.user_name").value("NewUser"));

        verify(userService, times(1)).createUser(any(Users.class));
    }

    @Test
    @DisplayName("Should update user when user exists")
    void testUpdateUserWhenUserExists() throws Exception {
        // Arrange
        Users updatedUser = new Users();
        updatedUser.setUser_id(1L);
        updatedUser.setUser_name("UpdatedUser");

        when(userService.updateUser(eq(1L), any(Users.class))).thenReturn(updatedUser);

        // Act & Assert
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"user_name\":\"UpdatedUser\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(1))
                .andExpect(jsonPath("$.user_name").value("UpdatedUser"));

        verify(userService, times(1)).updateUser(eq(1L), any(Users.class));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent user")
    void testUpdateUserWhenUserDoesNotExist() throws Exception {
        // Arrange
        when(userService.updateUser(eq(1L), any(Users.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"user_name\":\"UpdatedUser\"}"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).updateUser(eq(1L), any(Users.class));
    }

    @Test
    @DisplayName("Should delete user when user exists")
    void testDeleteUserWhenUserExists() throws Exception {
        // Arrange
        when(userService.deleteUser(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent user")
    void testDeleteUserWhenUserDoesNotExist() throws Exception {
        // Arrange
        when(userService.deleteUser(1L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).deleteUser(1L);
    }
}