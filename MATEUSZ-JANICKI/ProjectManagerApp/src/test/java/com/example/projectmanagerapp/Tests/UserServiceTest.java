package com.example.projectmanagerapp.Tests;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.example.projectmanagerapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should return all users")
    void testGetAllUsers() {
        User user1 = new User();
        user1.setUsername("TestUser1");

        User user2 = new User();
        user2.setUsername("TestUser2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a user")
    void testCreateUser() {
        User user = new User();
        user.setUsername("NewUser");

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertEquals("NewUser", result.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should find user by ID")
    void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("FoundUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("FoundUser", result.get().getUsername());
    }

    @Test
    @DisplayName("Should delete user by ID")
    void testDeleteUserById() {
        userService.deleteUserById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should update existing user")
    void testUpdateUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("OldName");

        User updatedUser = new User();
        updatedUser.setUsername("NewName");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(1L, updatedUser);

        assertEquals("NewName", result.getUsername());
        verify(userRepository, times(1)).save(existingUser);
    }
}
