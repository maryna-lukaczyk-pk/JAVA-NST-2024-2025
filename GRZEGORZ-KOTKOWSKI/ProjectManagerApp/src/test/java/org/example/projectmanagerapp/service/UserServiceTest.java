package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should return user by ID when it exists")
    void testGetUserById_WhenUserExists() {
        Long userId = 1L;
        Users existingUser = new Users();
        existingUser.setId(userId);
        existingUser.setUsername("ExistingUser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        Users result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals("ExistingUser", result.getUsername());
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should throw RuntimeException when user does not exist")
    void testGetUserById_WhenUserDoesNotExist() {
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUserById(userId));

        assertTrue(exception.getMessage().contains("User with ID " + userId + " not found."));
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should update existing user")
    void testUpdateUser_WhenUserExists() {
        Long userId = 1L;
        Users existingUser = new Users();
        existingUser.setId(userId);
        existingUser.setUsername("OldUsername");

        Users updatedData = new Users();
        updatedData.setUsername("NewUsername");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Users result = userService.updateUser(userId, updatedData);

        assertNotNull(result);
        assertEquals("NewUsername", result.getUsername());
        verify(userRepository).findById(userId);
        verify(userRepository).save(any(Users.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when updating non-existing user")
    void testUpdateUser_WhenUserDoesNotExist() {
        Long userId = 999L;
        Users updatedData = new Users();
        updatedData.setUsername("NewUsername");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUser(userId, updatedData));

        assertTrue(exception.getMessage().contains("User with ID " + userId + " not found."));
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    @DisplayName("Should delete user when it exists")
    void testDeleteUser_WhenUserExists() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Should throw RuntimeException when user does not exist on delete")
    void testDeleteUser_WhenUserDoesNotExist() {
        Long userId = 999L;
        when(userRepository.existsById(userId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.deleteUser(userId));

        assertTrue(exception.getMessage().contains("User with ID " + userId + " does not exist."));
        verify(userRepository).existsById(userId);
        verify(userRepository, never()).deleteById(userId);
    }
}