package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Users;
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
        userService = new UserService();
        try {
            var field = UserService.class.getDeclaredField("userRepository");
            field.setAccessible(true);
            field.set(userService, userRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Powinien zwrócić wszystkich użytkowników")
    void testGetAllUsers() {
        Users user1 = new Users();
        user1.setId(1L);
        user1.setUsername("User1");
        Users user2 = new Users();
        user2.setId(2L);
        user2.setUsername("User2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<Users> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Powinien zwrócić użytkownika po ID, gdy istnieje")
    void testGetUserByIdFound() {
        Users user = new Users();
        user.setId(1L);
        user.setUsername("User1");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<Users> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("User1", result.get().getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Powinien zwrócić pusty Optional, gdy użytkownik nie istnieje")
    void testGetUserByIdNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Users> result = userService.getUserById(99L);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Powinien stworzyć nowego użytkownika")
    void testCreateUser() {
        Users user = new Users();
        user.setUsername("NewUser");

        when(userRepository.save(user)).thenReturn(user);

        Users created = userService.createUser(user);

        assertEquals("NewUser", created.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Powinien zaktualizować istniejącego użytkownika")
    void testUpdateUserFound() {
        Users existing = new Users();
        existing.setId(1L);
        existing.setUsername("OldName");

        Users update = new Users();
        update.setUsername("NewName");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        Users updated = userService.updateUser(1L, update);

        assertNotNull(updated);
        assertEquals("NewName", updated.getUsername());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existing);
    }

    @Test
    @DisplayName("Powinien zwrócić null przy próbie aktualizacji nieistniejącego użytkownika")
    void testUpdateUserNotFound() {
        Users update = new Users();
        update.setUsername("NewName");
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Users updated = userService.updateUser(99L, update);

        assertNull(updated);
        verify(userRepository, times(1)).findById(99L);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Powinien usunąć użytkownika po ID")
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}