
package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Users;
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
    @DisplayName("Should return all users")
    void testGetAllUsers() {
        Users user1 = new Users();
        user1.setUsername("User1");
        Users user2 = new Users();
        user2.setUsername("User2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<Users> users = userService.getAllUsers();
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a user")
    void testCreateUser() {
        Users user = new Users();
        user.setUsername("NewUser");

        when(userRepository.save(user)).thenReturn(user);

        Users created = userService.createUser(user);
        assertEquals("NewUser", created.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should update a user")
    void testUpdateUser() {
        Users existing = new Users();
        existing.setUsername("Old");

        Users updated = new Users();
        updated.setUsername("New");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        Users result = userService.updateUser(1L, updated);
        assertEquals("New", result.getUsername());
        verify(userRepository).findById(1L);
        verify(userRepository).save(existing);
    }

    @Test
    @DisplayName("Should delete a user")
    void testDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should get user by ID")
    void testGetUserById() {
        Users user = new Users();
        user.setId(1L);
        user.setUsername("User1");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Users result = userService.getUserById(1L);
        assertEquals("User1", result.getUsername());
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception user not found by ID")
    void testGetUserByIdThrows() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(99L));
    }

    @Test
    @DisplayName("Should throw exception deleting non-existent user")
    void testDeleteUserThrows() {
        when(userRepository.existsById(99L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(99L));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void testUpdateUserThrows() {
        Users updated = new Users();
        updated.setUsername("ShouldFail");
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(99L, updated));
    }


}
