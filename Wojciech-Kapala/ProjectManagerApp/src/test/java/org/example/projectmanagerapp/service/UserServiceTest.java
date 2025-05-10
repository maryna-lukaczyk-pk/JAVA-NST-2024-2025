package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

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
    @DisplayName("Should return all users")
    void testGetAllUsers() {
        User u1 = new User(); u1.setUsername("A");
        User u2 = new User(); u2.setUsername("B");
        when(userRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find user by ID")
    void testGetUserById() {
        User u = new User(); u.setId(5L); u.setUsername("X");
        when(userRepository.findById(5L)).thenReturn(Optional.of(u));

        User result = userService.getUserById(5L);

        assertEquals("X", result.getUsername());
        verify(userRepository, times(1)).findById(5L);
    }

    @Test
    @DisplayName("Should throw when user not found by ID")
    void testGetUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
    }

    @Test
    @DisplayName("Should create user")
    void testCreateUser() {
        User u = new User(); u.setUsername("New");
        when(userRepository.save(u)).thenReturn(u);

        User result = userService.createUser(u);

        assertSame(u, result);
        verify(userRepository, times(1)).save(u);
    }

    @Test
    @DisplayName("Should update existing user")
    void testUpdateUser() {
        User existing = new User(); existing.setId(2L); existing.setUsername("Old");
        User dto      = new User(); dto.setUsername("New");
        when(userRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        User result = userService.updateUser(2L, dto);

        assertEquals("New", result.getUsername());
        verify(userRepository).findById(2L);
        verify(userRepository).save(existing);
    }

    @Test
    @DisplayName("Should delete user")
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(3L);
        userService.deleteUser(3L);
        verify(userRepository, times(1)).deleteById(3L);
    }
}
