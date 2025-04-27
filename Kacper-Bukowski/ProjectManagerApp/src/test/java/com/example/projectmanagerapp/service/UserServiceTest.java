package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should return all users")
    void getAllUsers() {
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
    @DisplayName("Should create a new user")
    void createUser() {
        User user = new User();
        user.setUsername("NewUser");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("NewUser", createdUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should return user by ID")
    void getUserById() {
        User user = new User();
        user.setUsername("TestUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals("TestUser", foundUser.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when user not found by ID")
    void getUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should update user")
    void updateUser() {
        User existingUser = new User();
        existingUser.setUsername("OldUsername");

        User updatedDetails = new User();
        updatedDetails.setUsername("NewUsername");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userService.updateUser(1L, updatedDetails);

        assertNotNull(updatedUser);
        assertEquals("NewUsername", updatedUser.getUsername());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    @DisplayName("Should delete user")
    void deleteUser() {
        User user = new User();
        user.setUsername("UserToDelete");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("Should throw exception when user not found during delete")
    void deleteUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found during update")
    void updateUser_NotFound() {
        User updatedDetails = new User();
        updatedDetails.setUsername("UpdatedUser");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(1L, updatedDetails);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }
}
