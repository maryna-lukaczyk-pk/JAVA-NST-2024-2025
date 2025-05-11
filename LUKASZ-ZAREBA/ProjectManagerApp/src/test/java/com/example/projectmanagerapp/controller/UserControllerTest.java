package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getAllUsers_returnsListOfUsers() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        List<User> users = Collections.singletonList(user);
        given(userService.getAll()).willReturn(users);

        List<User> result = userController.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("testuser", result.get(0).getUsername());
        verify(userService).getAll();
    }

    @Test
    void getUserById_returnsUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        given(userService.getById(1)).willReturn(user);

        User result = userController.getUserById(1);

        assertEquals(1, result.getId());
        assertEquals("testuser", result.getUsername());
        verify(userService).getById(1);
    }

    @Test
    void createUser_returnsCreatedUser() {
        User inputUser = new User();
        inputUser.setUsername("newuser");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setUsername("newuser");

        given(userService.create(any(User.class))).willReturn(savedUser);

        User result = userController.createUser(inputUser);

        assertEquals(1, result.getId());
        assertEquals("newuser", result.getUsername());
        verify(userService).create(inputUser);
    }

    @Test
    void updateUser_returnsUpdatedUser() {
        User inputUser = new User();
        inputUser.setUsername("updateduser");

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setUsername("updateduser");

        given(userService.update(eq(1), any(User.class))).willReturn(updatedUser);

        User result = userController.updateUser(1, inputUser);

        assertEquals(1, result.getId());
        assertEquals("updateduser", result.getUsername());
        verify(userService).update(1, inputUser);
    }

    @Test
    void deleteUser_returnsNoContent() {
        doNothing().when(userService).delete(1);

        ResponseEntity<Void> result = userController.deleteUser(1);

        assertEquals(204, result.getStatusCodeValue());
        assertNull(result.getBody());
        verify(userService).delete(1);
    }
}