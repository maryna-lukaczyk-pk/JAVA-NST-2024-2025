package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Zwraca wszystkich userów")
    void testGetAllUsers() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("TestUser1");

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("TestUser2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Tworzy nowego usera")
    void testCreateUser() {
        User userToCreate = new User();
        userToCreate.setUsername("NewUser");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setUsername("NewUser");

        when(userRepository.save(userToCreate)).thenReturn(savedUser);

        User result = userService.createUser(userToCreate);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("NewUser", result.getUsername());
        verify(userRepository, times(1)).save(userToCreate);
    }

    @Test
    @DisplayName("Zwraca usera po id")
    void testGetUserByIdExists() {
        User user = new User();
        user.setId(1);
        user.setUsername("TestUser");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1);

        assertTrue(result.isPresent());
        assertEquals("TestUser", result.get().getUsername());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Zwraca None kiedy nie istnieje")
    void testGetUserByIdNotExists() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(1);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Usuwa usera po id")
    void testDeleteUserById() {
        userService.deleteUserById(1);

        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Update usera jezeli istnieje")
    void testUpdateUserExists() {
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername("OldUsername");

        User userDetails = new User();
        userDetails.setUsername("NewUsername");

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setUsername("NewUsername");

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(1, userDetails);

        assertNotNull(result);
        assertEquals("NewUsername", result.getUsername());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Zwraca null kiedy nie istnieje")
    void testUpdateUserNotExists() {
        User userDetails = new User();
        userDetails.setUsername("NewUsername");

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        User result = userService.updateUser(1, userDetails);

        assertNull(result);
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, never()).save(any(User.class));
    }
}