package org.example.projectmanagerapp.tests.controller;

import org.example.projectmanagerapp.controller.UserController;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setup() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void getUsers_returnsAllUsers() {
        List<User> users = List.of(new User(), new User());
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void addUser_createsUserAndReturnsCreatedResponse() {
        User user = new User();
        user.setId(5L);
        when(userService.createUser(user)).thenReturn(user);

        ResponseEntity<User> response = userController.addUser(user);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(URI.create("/api/users/5"), response.getHeaders().getLocation());
        assertEquals(5L, response.getBody().getId());
        verify(userService, times(1)).createUser(user);
    }

    @Test
    void updateUser_updatesUserAndReturnsOk() {
        User user = new User();
        user.setId(10L);
        when(userService.updateUser(10L, user)).thenReturn(user);

        ResponseEntity<User> response = userController.updateUser(user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(10L, response.getBody().getId());
        verify(userService, times(1)).updateUser(10L, user);
    }

    @Test
    void deleteUser_deletesUserAndReturnsNoContent() {
        doNothing().when(userService).deleteUser(3L);

        ResponseEntity<Void> response = userController.deleteUser(3L);

        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(3L);
    }

    @Test
    void getUserById_returnsUserWhenFound() {
        User user = new User();
        user.setId(7L);
        when(userService.getUserById(7L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(7L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(7L, response.getBody().getId());
        verify(userService, times(1)).getUserById(7L);
    }

    @Test
    void getUserById_returnsNotFoundWhenUserMissing() {
        when(userService.getUserById(999L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(999L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(userService, times(1)).getUserById(999L);
    }
}

