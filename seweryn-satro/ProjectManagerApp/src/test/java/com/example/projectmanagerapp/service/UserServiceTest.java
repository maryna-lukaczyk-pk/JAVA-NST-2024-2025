package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    void testAllUsers() {
        User userOne = new User();
        userOne.setUsername("TestUser1");

        User userTwo = new User();
        userTwo.setUsername("TestUser2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(userOne, userTwo));

        List<User> allUsers = userService.allUsers();

        assertEquals(2, allUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should save new user")
    void testNewUser() {
        User newUser = new User();
        newUser.setUsername("NewUser");

        when(userRepository.save(newUser)).thenReturn(newUser);

        User savedUser = userService.newUser(newUser);

        assertEquals("NewUser", savedUser.getUsername());
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    @DisplayName("Should update existing user")
    void testUpdateUser() {
        User existingUser = new User();
        existingUser.setUsername("OldName");

        User updateRequest = new User();
        updateRequest.setUsername("UpdatedName");
        updateRequest.setProjects(Collections.emptySet());

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User updatedUser = userService.update(1, updateRequest);

        assertEquals("UpdatedName", updatedUser.getUsername());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    @DisplayName("Should return null if updated user is not found")
    void testUpdateUserNotFound() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        User result = userService.update(99, new User());

        assertNull(result);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete user if exists")
    void testDeleteUser() {
        when(userRepository.existsById(1)).thenReturn(true);

        userService.delete(1);

        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Should not delete user if does not exist")
    void testDeleteUserNotFound() {
        when(userRepository.existsById(99)).thenReturn(false);

        userService.delete(99);

        verify(userRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Should return user by ID")
    void testUserById() {
        User expectedUser = new User();
        expectedUser.setUsername("UserById");

        when(userRepository.findById(1)).thenReturn(Optional.of(expectedUser));

        User result = userService.userById(1);

        assertNotNull(result);
        assertEquals("UserById", result.getUsername());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should return null if user by ID not found")
    void testUserByIdNotFound() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        User result = userService.userById(99);

        assertNull(result);
        verify(userRepository, times(1)).findById(99);
    }
}
