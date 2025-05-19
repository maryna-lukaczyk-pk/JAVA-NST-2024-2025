package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Powinno zwrócić wszystkich użytkowników")
    void shouldReturnAllUsers() {
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
    @DisplayName("Powinno utworzyć nowego użytkownika")
    void shouldCreateUser() {
        User user = new User();
        user.setUsername("NewUser");

        when(userRepository.save(user)).thenReturn(user);

        User created = userService.createUser(user);

        assertNotNull(created);
        assertEquals("NewUser", created.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Powinno zwrócić użytkownika po ID")
    void shouldReturnUserById() {
        User user = new User();
        user.setUsername("TestUser");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> found = userService.getUserById(1L);

        assertTrue(found.isPresent());
        assertEquals("TestUser", found.get().getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Powinno zaktualizować użytkownika")
    void shouldUpdateUser() {
        User existingUser = new User();
        existingUser.setUsername("OldName");
        User updatedUser = new User();
        updatedUser.setUsername("NewName");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.updateUser(1L, updatedUser);

        assertEquals("NewName", result.getUsername());
        verify(userRepository).findById(1L);
        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Powinno rzucić wyjątek, gdy użytkownik do aktualizacji nie istnieje")
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.updateUser(1L, new User()));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("Powinno usunąć użytkownika po ID")
    void shouldDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
