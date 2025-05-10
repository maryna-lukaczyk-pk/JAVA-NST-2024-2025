package org.example.projectmanagerapp;

import org.example.projectmanagerapp.services.UserService;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.UserRepository;
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
        Users user1 = new Users();
        user1.setUsername("TESTuser1");

        Users user2 = new Users();
        user2.setUsername("TESTuser2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<Users> users = userService.getAllUsers();
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return no users")
    void testGetAllUsersEmpty() {
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        List<Users> users = userService.getAllUsers();
        assertEquals(0, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a new user")
    void testCreateUser() {
        Users user = new Users();
        user.setUsername("NewUser");

        when(userRepository.save(user)).thenReturn(user);

        Users created = userService.createUser(user);

        assertEquals("NewUser", created.getUsername());
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
        long userId = 2L;
        Users existingUser = new Users();
        existingUser.setUsername("OldUser");

        Users updatedUser = new Users();
        updatedUser.setUsername("UpdatedUser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        Users result = userService.updateUser(userId, updatedUser);

        assertEquals("UpdatedUser", result.getUsername());
        verify(userRepository).findById(userId);
        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Should throw when updating non-existing user")
    void testUpdateUserNotFound() {
        long userId = 3L;
        Users updatedUser = new Users();
        updatedUser.setUsername("TESTUSER123");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.updateUser(userId, updatedUser));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should return user by ID")
    void testGetUserById() {
        long userId = 4L;
        Users user = new Users();
        user.setUsername("UserById");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Users result = userService.getUserById(userId);

        assertEquals("UserById", result.getUsername());
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should throw when user not found by ID")
    void testGetUserByIdNotFound() {
        long userId = 5L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.getUserById(userId));

        assertEquals("Task not found", exception.getMessage());
    }
}