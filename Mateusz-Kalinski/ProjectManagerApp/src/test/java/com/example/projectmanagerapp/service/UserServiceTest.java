package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private Users user1;
    private Users user2;

    @BeforeEach
    void setUp() {
        user1 = new Users();
        user1.setId(1L);
        user1.setUsername("TestUser1");

        user2 = new Users();
        user2.setId(2L);
        user2.setUsername("TestUser2");
    }

    @Test
    @DisplayName("Powinien zwrócić wszystkich użytkowników")
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<Users> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Powinien zwrócić użytkownika po ID, gdy użytkownik istnieje")
    void testGetUserById_whenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        Optional<Users> foundUserOptional = userService.getUserById(1L);

        assertTrue(foundUserOptional.isPresent());
        assertEquals(user1.getUsername(), foundUserOptional.get().getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Powinien zwrócić pusty Optional, gdy użytkownik o danym ID nie istnieje")
    void testGetUserById_whenUserDoesNotExist() {
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        Optional<Users> foundUserOptional = userService.getUserById(3L);

        assertFalse(foundUserOptional.isPresent());
        verify(userRepository, times(1)).findById(3L);
    }

    @Test
    @DisplayName("Powinien utworzyć nowego użytkownika")
    void testCreateUser() {
        Users newUser = new Users();
        newUser.setUsername("NewUser");

        when(userRepository.save(any(Users.class))).thenAnswer(invocation -> {
            Users u = invocation.getArgument(0);
            if (u.getId() == null) { // Symulacja nadania ID tylko jeśli nie istnieje
                u.setId(3L);
            }
            return u;
        });

        Users createdUser = userService.createUser(newUser);

        assertNotNull(createdUser);
        assertEquals("NewUser", createdUser.getUsername());
        assertNotNull(createdUser.getId());
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    @DisplayName("Powinien zaktualizować istniejącego użytkownika")
    void testUpdateUser_whenUserExists() {
        Users userDetailsToUpdate = new Users();
        userDetailsToUpdate.setUsername("UpdatedUser");

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.save(any(Users.class))).thenAnswer(invocation -> {
            Users u = invocation.getArgument(0);
            // Upewniamy się, że ID jest zachowane/ustawione poprawnie przed zapisem
            if (u.getId() == null) u.setId(1L);
            return u;
        });


        Users updatedUser = userService.updateUser(1L, userDetailsToUpdate);

        assertNotNull(updatedUser);
        assertEquals("UpdatedUser", updatedUser.getUsername());
        assertEquals(1L, updatedUser.getId());
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).save(any(Users.class));
    }

    @Test
    @DisplayName("Powinien zwrócić null podczas aktualizacji, gdy użytkownik nie istnieje")
    void testUpdateUser_whenUserDoesNotExist() {
        Users userDetailsToUpdate = new Users();
        userDetailsToUpdate.setUsername("UpdatedUser");
        when(userRepository.existsById(3L)).thenReturn(false);

        Users result = userService.updateUser(3L, userDetailsToUpdate);

        assertNull(result);
        verify(userRepository, times(1)).existsById(3L);
        verify(userRepository, never()).save(any(Users.class));
    }


    @Test
    @DisplayName("Powinien usunąć użytkownika po ID")
    void testDeleteUser_whenUserExists() {
        doNothing().when(userRepository).deleteById(1L);
        // Można dodać when(userRepository.existsById(1L)).thenReturn(true); jeśli serwis to sprawdza
        // przed wywołaniem deleteById, aby uniknąć EmptyResultDataAccessException, jeśli repozytorium by go rzucało.
        // W obecnej implementacji serwisu, po prostu wołamy deleteById.

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
