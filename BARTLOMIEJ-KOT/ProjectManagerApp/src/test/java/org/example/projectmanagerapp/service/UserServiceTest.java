package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

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
    @DisplayName("Pobiera wszystkich użytkowników")
    void getAllUsers() {
        User u1 = new User(); u1.setUsername("a");
        User u2 = new User(); u2.setUsername("b");

        when(userRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById() {
        User u = new User(); u.setUsername("qwe");
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        User result = userService.getUserById(1L);

        assertEquals("qwe", result.getUsername());
    }

    @Test
    void createUser() {
        User u = new User(); u.setEmail("x@example.com");
        when(userRepository.save(u)).thenReturn(u);

        User result = userService.createUser(u);

        assertEquals("x@example.com", result.getEmail());
    }

    @Test
    void updateUser() {
        User existing = new User(); existing.setUsername("old");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        User input = new User(); input.setUsername("new");
        User result = userService.updateUser(1L, input);

        assertEquals("new", result.getUsername());
    }

    @Test
    void deleteUser() {
        userService.deleteUser(99L);
        verify(userRepository).deleteById(99L);
    }
}
