package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
    void getAllUsers() {
        User user1 = new User();
        user1.setUsername("Alice");

        User user2 = new User();
        user2.setUsername("Bob");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should return user by ID")
    void getUserById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Charlie");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals("Charlie", result.getUsername());
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should save new user")
    void createUser() {
        User user = new User();
        user.setUsername("TestUser");

        when(userRepository.save(user)).thenReturn(user);

        User saved = userService.createUser(user);

        assertEquals("TestUser", saved.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should update user")
    void updateUser() {
        User existing = new User();
        existing.setId(1L);
        existing.setUsername("OldName");

        User updated = new User();
        updated.setUsername("NewName");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        User result = userService.updateUser(1L, updated);

        assertEquals("NewName", result.getUsername());
        verify(userRepository).findById(1L);
        verify(userRepository).save(existing);
    }

    @Test
    @DisplayName("Should delete user")
    void deleteUser() {
        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }
}
