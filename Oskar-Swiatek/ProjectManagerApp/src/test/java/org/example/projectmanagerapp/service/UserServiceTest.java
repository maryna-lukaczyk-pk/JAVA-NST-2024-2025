package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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
}