package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Test getAllUsers returns list of users")
    void testGetAllUsers() {
        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);
        List<User> users = Arrays.asList(user1, user2);

        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Test getUserById returns user when found")
    void testGetUserByIdFound() {
        User user = new User();
        user.setId(1);

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        Mockito.verify(userRepository, Mockito.times(1)).findById(1);
    }

    @Test
    @DisplayName("Test getUserById returns null when user not found")
    void testGetUserByIdNotFound() {
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());

        User result = userService.getUserById(1);
        assertNull(result);
        Mockito.verify(userRepository, Mockito.times(1)).findById(1);
    }

    @Test
    @DisplayName("Test createUser saves and returns the user")
    void testCreateUser() {
        User user = new User();
        user.setId(1);

        Mockito.when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);
        assertEquals(user.getId(), result.getId());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    @DisplayName("Test updateUser updates and returns user when found")
    void testUpdateUserFound() {
        User existingUser = new User();
        existingUser.setId(1);

        User updatedUser = new User();
        updatedUser.setUsername("Updated Name");
        updatedUser.setId(1);

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User result = userService.updateUser(1, updatedUser);
        assertNotNull(result);
        assertEquals("Updated Name", result.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).findById(1);
        Mockito.verify(userRepository, Mockito.times(1)).save(updatedUser);
    }

    @Test
    @DisplayName("Test updateUser returns null when user not found")
    void testUpdateUserNotFound() {
        User updatedUser = new User();
        updatedUser.setUsername("Updated Name");

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());

        User result = userService.updateUser(1, updatedUser);
        assertNull(result);
        Mockito.verify(userRepository, Mockito.times(1)).findById(1);
    }

    @Test
    @DisplayName("Test deleteUser calls deleteById on repository")
    void testDeleteUser() {
        userService.deleteUser(1);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1);
    }
}