package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    @DisplayName("getAllUsers() should return all users")
    void testGetAllUsers() {
        User u1 = new User(); u1.setId(1L); u1.setUsername("User1");
        User u2 = new User(); u2.setId(2L); u2.setUsername("User2");
        when(userRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getUserById() should return user when found")
    void testGetUserByIdFound() {
        User u = new User(); u.setId(10L); u.setUsername("FoundUser");
        when(userRepository.findById(10L)).thenReturn(Optional.of(u));

        User result = userService.getUserById(10L);

        assertNotNull(result);
        assertEquals("FoundUser", result.getUsername());
        verify(userRepository, times(1)).findById(10L);
    }

    @Test
    @DisplayName("getUserById() should return null when not found")
    void testGetUserByIdNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        User result = userService.getUserById(99L);

        assertNull(result);
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("createUser() should save and return user")
    void testCreateUser() {
        User input = new User(); input.setUsername("NewUser");
        User saved = new User(); saved.setId(5L); saved.setUsername("NewUser");
        when(userRepository.save(input)).thenReturn(saved);

        User result = userService.createUser(input);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("NewUser", result.getUsername());
        verify(userRepository, times(1)).save(input);
    }

    @Test
    @DisplayName("updateUser() should update existing user")
    void testUpdateUserFound() {
        User existing = new User(); existing.setId(20L); existing.setUsername("Old");
        User updateData = new User(); updateData.setId(20L); updateData.setUsername("Updated");
        when(userRepository.findById(20L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(updateData);

        assertNotNull(result);
        assertEquals(20L, result.getId());
        assertEquals("Updated", result.getUsername());
        verify(userRepository).findById(20L);
        verify(userRepository).save(existing);
    }

    @Test
    @DisplayName("updateUser() should return null when user not found")
    void testUpdateUserNotFound() {
        User updateData = new User(); updateData.setId(99L); updateData.setUsername("X");
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        User result = userService.updateUser(updateData);

        assertNull(result);
        verify(userRepository).findById(99L);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteUser() should delete and return existing user")
    void testDeleteUserFound() {
        User existing = new User(); existing.setId(30L); existing.setUsername("ToDelete");
        when(userRepository.findById(30L)).thenReturn(Optional.of(existing));

        User result = userService.deleteUser(30L);

        assertNotNull(result);
        assertEquals(30L, result.getId());
        verify(userRepository).findById(30L);
        verify(userRepository).delete(existing);
    }

    @Test
    @DisplayName("deleteUser() should return null when user not found")
    void testDeleteUserNotFound() {
        when(userRepository.findById(123L)).thenReturn(Optional.empty());

        User result = userService.deleteUser(123L);

        assertNull(result);
        verify(userRepository).findById(123L);
        verify(userRepository, never()).delete(any());
    }
}
