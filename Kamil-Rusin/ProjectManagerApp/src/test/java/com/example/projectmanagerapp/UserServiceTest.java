package com.example.projectmanagerapp;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import com.example.projectmanagerapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserServiceTest {
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
        User user1 = new User();
        user1.setUserName("User1");

        User user2 = new User();
        user2.setUserName("User2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAll();

        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should return user by ID")
    void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setUserName("TestUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("TestUser", result.get().getUserName());
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should create a new user")
    void testCreateUser() {
        User user = new User();
        user.setUserName("NewUser");

        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.createUser(user);

        assertEquals("NewUser", savedUser.getUserName());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should update existing user")
    void testUpdateExistingUser() {
        User existing = new User();
        existing.setId(1L);
        existing.setUserName("OldName");

        User updated = new User();
        updated.setUserName("UpdatedName");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser(1L, updated);

        assertEquals("UpdatedName", result.getUserName());
        verify(userRepository).save(existing);
    }

    @Test
    @DisplayName("Should save user if not found when updating")
    void testUpdateNonExistingUser() {
        User user = new User();
        user.setUserName("FallbackUser");

        when(userRepository.findById(42L)).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(42L, user);

        assertEquals("FallbackUser", result.getUserName());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should delete user by ID")
    void testDeleteUser() {
        userService.deleteUser(5L);
        verify(userRepository).deleteById(5L);
    }
}
