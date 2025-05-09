package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repositories.UserRepository;
import org.example.projectmanagerapp.services.UserService;
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
    void testGetAllUsers() {
        User user1 = new User();
        user1.setUserName("TestUser1");

        User user2 = new User();
        user2.setUserName("TestUser2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create user")
    void testCreateUser() {
        User user = new User();
        user.setUserName("NewUser");

        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("NewUser", createdUser.getUserName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should find user by ID")
    void testGetUserById() {
        User user = new User();
        user.setId(1);
        user.setUserName("User1");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> found = userService.getUserById(1);

        assertTrue(found.isPresent());
        assertEquals("User1", found.get().getUserName());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should delete user by ID")
    void testDeleteUser() {
        userService.deleteUser(1);

        verify(userRepository, times(1)).deleteById(1);
    }
}