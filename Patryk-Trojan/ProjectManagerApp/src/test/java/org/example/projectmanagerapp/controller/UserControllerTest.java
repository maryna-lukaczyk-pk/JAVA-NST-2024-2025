package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = Mockito.mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    @DisplayName("GET /api/users - Should return all users")
    public void shouldReturnAllUsers() {
        // Given
        User user1 = new User();
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUsername("user2");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        // When
        List<User> result = userController.getAllUsers();

        // Then
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("GET /api/users/{id} - Should return user by ID")
    public void shouldReturnUserById() {
        // Given
        User user = new User();
        user.setUsername("testUser");
        when(userService.findUserById(1L)).thenReturn(user);

        // When
        User result = userController.getUserById(1L);

        // Then
        assertEquals("testUser", result.getUsername());
        verify(userService, times(1)).findUserById(1L);
    }

    @Test
    @DisplayName("POST /api/users - Should create new user")
    public void shouldCreateUser() {
        // Given
        User inputUser = new User();
        inputUser.setUsername("newUser");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("newUser");

        when(userService.createUser(inputUser)).thenReturn(savedUser);

        // When
        User result = userController.createUser(inputUser);

        // Then
        assertEquals(1L, result.getId());
        assertEquals("newUser", result.getUsername());
        verify(userService, times(1)).createUser(inputUser);
    }

    @Test
    @DisplayName("DELETE /api/users/{id} - Should delete user by ID")
    public void shouldDeleteUserById() {
        // Given
        doNothing().when(userService).deleteUser(1L);

        // When
        userController.deleteUser(1L);

        // Then
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    @DisplayName("DELETE /api/users - Should delete all users")
    public void shouldDeleteAllUsers() {
        // Given
        doNothing().when(userService).deleteAllUsers();

        // When
        userController.deleteAllUsers();

        // Then
        verify(userService, times(1)).deleteAllUsers();
    }

    @Test
    @DisplayName("PUT /api/users/{id} - Should update user")
    public void shouldUpdateUser() {
        // Given
        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");
        doNothing().when(userService).updateUser(1L, updatedUser);

        // When
        userController.updateUser(updatedUser, 1L);

        // Then
        verify(userService, times(1)).updateUser(1L, updatedUser);
    }
}