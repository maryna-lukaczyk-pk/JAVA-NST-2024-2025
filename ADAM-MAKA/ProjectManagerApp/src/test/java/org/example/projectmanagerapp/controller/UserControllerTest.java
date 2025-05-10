package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() throws Exception {
        userService = Mockito.mock(UserService.class);
        userController = new UserController(userService);

        java.lang.reflect.Field userServiceField = UserController.class.getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(userController, userService);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("user2");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userController.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserByIdExists() {
        User user = new User();
        user.setId(1);
        user.setUsername("existingUser");

        when(userService.getUserById(1)).thenReturn(user);

        ResponseEntity<User> response = userController.getUserById(1);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("existingUser", Objects.requireNonNull(response.getBody()).getUsername());

        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userService.getUserById(100)).thenReturn(null);

        ResponseEntity<User> response = userController.getUserById(100);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        verify(userService, times(1)).getUserById(100);
    }

    @Test
    void testCreateUser() {
        User inputUser = new User();
        inputUser.setUsername("newUser");

        User createdUser = new User();
        createdUser.setId(1);
        createdUser.setUsername("newUser");

        when(userService.createUser(inputUser)).thenReturn(createdUser);

        User result = userController.createUser(inputUser);
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("newUser", result.getUsername());

        verify(userService, times(1)).createUser(inputUser);
    }

    @Test
    void testUpdateUser() {
        User updatedData = new User();
        updatedData.setUsername("updatedUser");

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setUsername("updatedUser");

        when(userService.updateUser(1, updatedData)).thenReturn(updatedUser);

        User result = userController.updateUser(1, updatedData);
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("updatedUser", result.getUsername());

        verify(userService, times(1)).updateUser(1, updatedData);
    }
}