// src/test/java/com/example/projectmanagerapp/service/UserServiceTest.java
package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        userRepository = mock(UserRepository.class);
        userService   = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should return all users")
    void testGetAllUsers() {
        User u1 = new User(); u1.setUsername("TestUser1");
        User u2 = new User(); u2.setUsername("TestUser2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<User> users = userService.getUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find user by ID")
    void testGetUserById() {
        User u = new User(); u.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        User result = userService.getUserById(1L);

        assertEquals(1L, result.getId());
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should create user")
    void testCreateUser() {
        User u = new User(); u.setUsername("New");
        when(userRepository.save(u)).thenReturn(u);

        User result = userService.createUser(u);

        assertEquals("New", result.getUsername());
        verify(userRepository).save(u);
    }

    void testUpdateUser() {
        User existing = new User(); existing.setId(1L);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existing));
        // return the argument passed into save()
        when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        User toUpdate = new User(); toUpdate.setUsername("Upd");
        User result   = userService.updateUser(1L, toUpdate);

        assertEquals("Upd", result.getUsername());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should delete user")
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
    void testGetUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> userService.getUserById(99L),
                "User not found with id 99");
        verify(userRepository).findById(99L);
    }

    @Test
    @DisplayName("updateUser() – not found should throw")
    void testUpdateUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> userService.updateUser(99L, new User()),
                "User not found with id 99");
        verify(userRepository).findById(99L);
    }
}
