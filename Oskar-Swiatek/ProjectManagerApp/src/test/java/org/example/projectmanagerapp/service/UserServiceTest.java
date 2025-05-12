package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
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
    @DisplayName("Should return all users from repository")
    void testGetAllUsers() {
        // given
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("TestUser1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("TestUser2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // when
        List<User> users = userService.getAllUsers();

        //then
        assertEquals(2, users.size());
        assertEquals("TestUser1", users.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return user by ID when exists")
    void testGetUserById_WhenUserExists() {
        // given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("TestUser1");

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        // when
        User result = userService.getUserById(userId);

        // then
        assertNotNull(result);
        assertEquals("TestUser1", result.getUsername());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void testGetUserById_WhenUserNotFound() {
        // given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(userId);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should create and return new user")
    void testCreateUser() {
        // given
        User userToCreate = new User();
        userToCreate.setUsername("NewUser");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("NewUser");

        when(userRepository.save(userToCreate)).thenReturn(savedUser);

        // when
        User result = userService.createUser(userToCreate);

        // then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("NewUser", result.getUsername());
        verify(userRepository, times(1)).save(userToCreate);
    }

    @Test
    @DisplayName("Should update user when user exists")
    void testUpdateUser_WhenUserExists() {
        // given
        Long userId = 1L;

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("OldName");

        User updatedDetails = new User();
        updatedDetails.setUsername("UpdatedName");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // when
        User result = userService.updateUser(userId, updatedDetails);

        // then
        assertNotNull(result);
        assertEquals("UpdatedName", result.getUsername());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void testUpdateUser_WhenUserNotFound() {
        // given
        Long userId = 99L;
        User userDetails = new User();
        userDetails.setUsername("Whatever");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(userId, userDetails);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete user by ID")
    void testDeleteUser_WhenUserExists() {
        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        // when
        userService.deleteUser(userId);

        // then
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent user")
    void testDeleteUser_WhenUserNotFound() {
        // given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.deleteUser(userId);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).deleteById(any());
    }
}