package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import com.example.projectmanagerapp.exception.DuplicateResourceException;
import com.example.projectmanagerapp.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*; // Standardowe asercje JUnit
import static org.assertj.core.api.Assertions.assertThat; // Asercje AssertJ (bardziej czytelne)
import static org.mockito.Mockito.*; // Metody Mockito

@ExtendWith(MockitoExtension.class) // Włącza rozszerzenie Mockito dla JUnit 5
class UserServiceTest {

    @Mock // Tworzy mocka dla UserRepository
    private UserRepository userRepository;

    @InjectMocks // Wstrzykuje mocki (userRepository) do testowanej klasy UserService
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach // Wykonywane przed każdym testem
    void setUp() {
        // Inicjalizacja danych testowych
        user1 = new User("testUser1");
        user1.setId(1L); // Ustawiamy ID dla celów testowych

        user2 = new User("testUser2");
        user2.setId(2L);
    }

    @Test
    @DisplayName("Should return all users when users exist")
    void getAllUsers_shouldReturnListOfUsers() {
        // Arrange (Przygotuj)
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act (Działaj)
        List<User> foundUsers = userService.getAllUsers();

        // Assert (Sprawdź)
        assertThat(foundUsers).isNotNull();
        assertThat(foundUsers).hasSize(2);
        assertThat(foundUsers).containsExactlyInAnyOrder(user1, user2);
        verify(userRepository, times(1)).findAll(); // Sprawdź, czy metoda findAll została wywołana raz
    }

    @Test
    @DisplayName("Should return empty list when no users exist")
    void getAllUsers_shouldReturnEmptyListWhenNoUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<User> foundUsers = userService.getAllUsers();

        // Assert
        assertThat(foundUsers).isNotNull();
        assertThat(foundUsers).isEmpty();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return user when user with given ID exists")
    void getUserById_shouldReturnUser_whenUserExists() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));

        // Act
        User foundUser = userService.getUserById(userId);

        // Assert
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(userId);
        assertThat(foundUser.getUsername()).isEqualTo(user1.getUsername());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user with given ID does not exist")
    void getUserById_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {
        // Arrange
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(userId);
        });
        assertThat(exception.getMessage()).isEqualTo("User not found with id: " + userId);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should create and return user when username is valid and not taken")
    void createUser_shouldCreateAndReturnUser_whenUsernameIsValid() {
        // Arrange
        String newUsername = "newUser";
        User userToSave = new User(newUsername); // User bez ID przed zapisem
        User savedUser = new User(newUsername);  // Symulacja Usera po zapisie z ID
        savedUser.setId(3L);

        when(userRepository.existsByUsername(newUsername)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser); // any(User.class) bo newUser nie ma ID, a savedUser ma

        // Act
        User createdUser = userService.createUser(newUsername);

        // Assert
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isEqualTo(savedUser.getId());
        assertThat(createdUser.getUsername()).isEqualTo(newUsername);
        verify(userRepository, times(1)).existsByUsername(newUsername);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when creating user with empty username")
    void createUser_shouldThrowIllegalArgumentException_whenUsernameIsEmpty() {
        // Arrange
        String emptyUsername = "";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(emptyUsername);
        });
        assertThat(exception.getMessage()).isEqualTo("Username cannot be empty.");
        verify(userRepository, never()).existsByUsername(anyString()); // existsByUsername nie powinno być wywołane
        verify(userRepository, never()).save(any(User.class)); // save nie powinno być wywołane
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when creating user with existing username")
    void createUser_shouldThrowDuplicateResourceException_whenUsernameExists() {
        // Arrange
        String existingUsername = "testUser1";
        when(userRepository.existsByUsername(existingUsername)).thenReturn(true);

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            userService.createUser(existingUsername);
        });
        assertThat(exception.getMessage()).isEqualTo("Username '" + existingUsername + "' already exists.");
        verify(userRepository, times(1)).existsByUsername(existingUsername);
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    @DisplayName("Should update and return user when ID exists and new username is valid")
    void updateUser_shouldUpdateUser_whenValid() {
        // Arrange
        Long userId = 1L;
        String newUsername = "updatedUser1";
        User existingUser = new User("testUser1"); // Użytkownik przed aktualizacją
        existingUser.setId(userId);

        User updatedUser = new User(newUsername); // Użytkownik po aktualizacji
        updatedUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername(newUsername)).thenReturn(false); // Zakładamy, że nowy username nie jest zajęty
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act
        User result = userService.updateUser(userId, newUsername);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getUsername()).isEqualTo(newUsername);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).existsByUsername(newUsername);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existing user")
    void updateUser_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {
        // Arrange
        Long userId = 99L;
        String newUsername = "anyUser";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(userId, newUsername);
        });
        assertThat(exception.getMessage()).isEqualTo("User not found with id: " + userId + " for update.");
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when updating user with empty username")
    void updateUser_shouldThrowIllegalArgumentException_whenNewUsernameIsEmpty() {
        // Arrange
        Long userId = 1L;
        String emptyUsername = "";
        User existingUser = new User("testUser1");
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser)); // Potrzebne, bo findById jest wołane przed walidacją username

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(userId, emptyUsername);
        });
        assertThat(exception.getMessage()).isEqualTo("New username cannot be empty.");
        verify(userRepository, times(1)).findById(userId); // findById jest wołane
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when new username is taken by another user")
    void updateUser_shouldThrowDuplicateResourceException_whenNewUsernameIsTaken() {
        // Arrange
        Long userId = 1L; // Użytkownik, którego aktualizujemy
        User existingUserToUpdate = new User("testUser1");
        existingUserToUpdate.setId(userId);

        String newUsername = "testUser2"; // Ten username jest już zajęty przez user2

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUserToUpdate));
        when(userRepository.existsByUsername(newUsername)).thenReturn(true);

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            userService.updateUser(userId, newUsername);
        });
        assertThat(exception.getMessage()).isEqualTo("Username '" + newUsername + "' is already taken by another user.");
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).existsByUsername(newUsername);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should allow updating user with their own existing username")
    void updateUser_shouldAllowUpdateWithOwnExistingUsername() {
        // Arrange
        Long userId = 1L;
        String currentUsername = "testUser1";
        User existingUser = new User(currentUsername);
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        // `existsByUsername` nie powinno być wołane, jeśli username się nie zmienia
        when(userRepository.save(any(User.class))).thenReturn(existingUser); // Zwraca tego samego użytkownika

        // Act
        User result = userService.updateUser(userId, currentUsername);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(currentUsername);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).existsByUsername(currentUsername); // Kluczowe: nie sprawdzamy duplikatu dla własnego username
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    @DisplayName("Should delete user when user exists")
    void deleteUser_shouldDeleteUser_whenUserExists() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId); // Mockowanie metody void

        // Act
        assertDoesNotThrow(() -> { // Sprawdzamy, czy nie rzuca wyjątku
            userService.deleteUser(userId);
        });

        // Assert
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existing user")
    void deleteUser_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {
        // Arrange
        Long userId = 99L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(userId);
        });
        assertThat(exception.getMessage()).isEqualTo("User not found with id: " + userId + " for deletion.");
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(userId);
    }
}