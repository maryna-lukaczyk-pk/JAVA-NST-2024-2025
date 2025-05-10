package com.example.projectmanagerapp.services;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should return all users")
    void testGetAllUsers() {
        User user1 = new User();
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUsername("user2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return no users")
    void testGetAllUsersEmpty() {
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        List<User> users = userService.getAllUsers();
        assertEquals(0, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a user")
    void testCreateUser() {
        User user = new User();
        user.setUsername("newuser");

        when(userRepository.save(user)).thenReturn(user);

        User created = userService.createUser(user);

        assertEquals("newuser", created.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should delete a user by ID")
    void testDeleteUser() {
        long userId = 1L;

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Should update an existing user")
    void testUpdateUser() {
        long userId = 1L;
        User existingUser = new User();
        existingUser.setUsername("olduser");

        User updatedUser = new User();
        updatedUser.setUsername("updateduser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.updateUser(userId, updatedUser);

        assertEquals("updateduser", result.getUsername());
        verify(userRepository).findById(userId);
        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Should throw when updating non-existent user")
    void testUpdateUserNotFound() {
        long userId = 99L;
        User updatedUser = new User();
        updatedUser.setUsername("any");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.updateUser(userId, updatedUser));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should return user by ID")
    void testGetUserById() {
        long userId = 1L;
        User user = new User();
        user.setUsername("byId");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);

        assertEquals("byId", result.getUsername());
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should throw when user not found by ID")
    void testGetUserByIdNotFound() {
        long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.getUserById(userId));

        assertEquals("Task not found", exception.getMessage());
    }
}