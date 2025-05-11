package org.example.projectmanagerapp;

import org.example.projectmanagerapp.controllers.*;

import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    @DisplayName("Should return all users via controller")
    void testGetUsers() {
        // Given
        Users u1 = new Users();
        u1.setUsername("UserA");
        Users u2 = new Users();
        u2.setUsername("UserB");
        when(userService.getAllUsers()).thenReturn(Arrays.asList(u1, u2));

        List<Users> users = userController.getUsers();

        // Then
        assertEquals(2, users.size());
        assertEquals("UserA", users.get(0).getUsername());
        assertEquals("UserB", users.get(1).getUsername());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Should return user by ID via controller")
    void testGetUserById() {
        long id = 1L;
        Users user = new Users();
        user.setId(id);
        user.setUsername("User321");
        when(userService.getUserById(id)).thenReturn(user);

        Users result = userController.getUserById(id);

        assertNotNull(result);
        assertEquals("User321", result.getUsername());
        verify(userService).getUserById(id);
    }

    @Test
    @DisplayName("Should create a user via controller")
    void testCreateUser() {
        Users input = new Users();
        input.setUsername("NewUser");
        Users saved = new Users();
        saved.setId(100L);
        saved.setUsername("NewUser");
        when(userService.createUser(input)).thenReturn(saved);

        Users result = userController.createUser(input);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("NewUser", result.getUsername());
        verify(userService).createUser(input);
    }

    @Test
    @DisplayName("Should update user via controller")
    void testUpdateUser() {
        long id = 2L;
        Users existing = new Users();
        existing.setId(id);
        existing.setUsername("OldName");

        Users updated = new Users();
        updated.setUsername("NewName");

        when(userService.updateUser(id, updated)).thenReturn(updated);

        Users result = userController.updateUser(id, updated);

        assertNotNull(result);
        assertEquals("NewName", result.getUsername());
        verify(userService).updateUser(id, updated);
    }

    @Test
    @DisplayName("Should delete user via controller")
    void testDeleteUser() {
        long id = 3L;
        doNothing().when(userService).deleteUser(id);

        userController.deleteUser(id);

        verify(userService).deleteUser(id);
    }
}