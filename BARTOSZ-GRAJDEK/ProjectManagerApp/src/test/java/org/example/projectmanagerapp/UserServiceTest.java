package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

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
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));

        userService.getAllUsers();
        verify(userRepository).findAll();
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

        userService.getUserById(1L);
        verify(userRepository).findById(1L);
    }

    @Test
    void testCreateUser() {
        User user = new User();

        when(userRepository.save(user)).thenReturn(user);

        userService.createUser(user);
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUser() {
        User existing = new User("oldUsername");
        User updated = new User("newUsername");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));

        userService.updateUser(1L, updated);
        verify(userRepository).save(existing);
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }
}
