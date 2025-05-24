// src/test/java/com/example/projectmanagerapp/service/UserServiceTest.java
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
import static org.mockito.ArgumentMatchers.any; // Dodano brakujący import


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.HashSet; // Dla inicjalizacji Set<Project>

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
        user1.setId(1L);
        user1.setProjects(new HashSet<>()); // Inicjalizacja kolekcji, aby uniknąć NullPointerException

        user2 = new User("testUser2");
        user2.setId(2L);
        user2.setProjects(new HashSet<>()); // Inicjalizacja kolekcji
    }

    @Test
    @DisplayName("Powinien zwrócić wszystkich użytkowników, gdy użytkownicy istnieją") // Przetłumaczono
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
    @DisplayName("Powinien zwrócić pustą listę, gdy nie ma użytkowników") // Przetłumaczono
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
    @DisplayName("Powinien zwrócić użytkownika, gdy użytkownik o podanym ID istnieje") // Przetłumaczono
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
    @DisplayName("Powinien rzucić ResourceNotFoundException, gdy użytkownik o podanym ID nie istnieje") // Przetłumaczono
    void getUserById_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {
        // Arrange
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(userId);
        });
        assertThat(exception.getMessage()).isEqualTo("Nie znaleziono użytkownika o ID: " + userId); // Zaktualizowano komunikat
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Powinien utworzyć i zwrócić użytkownika, gdy nazwa użytkownika jest poprawna i nie zajęta") // Przetłumaczono
    void createUser_shouldCreateAndReturnUser_whenUsernameIsValid() {
        // Arrange
        String newUsername = "newUser";
        User userToSave = new User(newUsername); // User bez ID przed zapisem
        User savedUser = new User(newUsername);  // Symulacja Usera po zapisie z ID
        savedUser.setId(3L);

        when(userRepository.existsByUsername(newUsername)).thenReturn(false);
        // any(User.class) jest używane, ponieważ obiekt przekazywany do save
        // (instancja User tworzona w createUser) może nie być dokładnie tym samym obiektem,
        // który mockujemy jako userToSave, ale powinien być typu User.
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

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
    @DisplayName("Powinien rzucić IllegalArgumentException podczas tworzenia użytkownika z pustą nazwą użytkownika") // Przetłumaczono
    void createUser_shouldThrowIllegalArgumentException_whenUsernameIsEmpty() {
        // Arrange
        String emptyUsername = "";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(emptyUsername);
        });
        assertThat(exception.getMessage()).isEqualTo("Nazwa użytkownika nie może być pusta."); // Zaktualizowano komunikat
        verify(userRepository, never()).existsByUsername(anyString()); // existsByUsername nie powinno być wywołane
        verify(userRepository, never()).save(any(User.class)); // save nie powinno być wywołane
    }

    @Test
    @DisplayName("Powinien rzucić DuplicateResourceException podczas tworzenia użytkownika z istniejącą nazwą użytkownika") // Przetłumaczono
    void createUser_shouldThrowDuplicateResourceException_whenUsernameExists() {
        // Arrange
        String existingUsername = "testUser1";
        when(userRepository.existsByUsername(existingUsername)).thenReturn(true);

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            userService.createUser(existingUsername);
        });
        assertThat(exception.getMessage()).isEqualTo("Nazwa użytkownika '" + existingUsername + "' już istnieje."); // Zaktualizowano komunikat
        verify(userRepository, times(1)).existsByUsername(existingUsername);
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    @DisplayName("Powinien zaktualizować i zwrócić użytkownika, gdy ID istnieje, a nowa nazwa użytkownika jest poprawna") // Przetłumaczono
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
        verify(userRepository, times(1)).existsByUsername(newUsername); // Sprawdzamy, czy nowa nazwa jest unikalna
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Powinien rzucić ResourceNotFoundException podczas aktualizacji nieistniejącego użytkownika") // Przetłumaczono
    void updateUser_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {
        // Arrange
        Long userId = 99L;
        String newUsername = "anyUser";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(userId, newUsername);
        });
        assertThat(exception.getMessage()).isEqualTo("Nie znaleziono użytkownika o ID: " + userId + " do aktualizacji."); // Zaktualizowano komunikat
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Powinien rzucić IllegalArgumentException podczas aktualizacji użytkownika z pustą nazwą użytkownika") // Przetłumaczono
    void updateUser_shouldThrowIllegalArgumentException_whenNewUsernameIsEmpty() {
        // Arrange
        Long userId = 1L;
        String emptyUsername = "";
        User existingUser = new User("testUser1");
        existingUser.setId(userId);

        // Potrzebne, bo findById jest wołane przed walidacją username
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(userId, emptyUsername);
        });
        assertThat(exception.getMessage()).isEqualTo("Nowa nazwa użytkownika nie może być pusta."); // Zaktualizowano komunikat
        verify(userRepository, times(1)).findById(userId); // findById jest wołane
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Powinien rzucić DuplicateResourceException, gdy nowa nazwa użytkownika jest zajęta przez innego użytkownika") // Przetłumaczono
    void updateUser_shouldThrowDuplicateResourceException_whenNewUsernameIsTaken() {
        // Arrange
        Long userId = 1L; // Użytkownik, którego aktualizujemy
        User existingUserToUpdate = new User("testUser1");
        existingUserToUpdate.setId(userId);

        String newUsername = "testUser2"; // Ten username jest już zajęty przez innego użytkownika (user2)

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUserToUpdate));
        // Założenie: "testUser2" istnieje w bazie i nie jest to "testUser1"
        when(userRepository.existsByUsername(newUsername)).thenReturn(true);

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            userService.updateUser(userId, newUsername);
        });
        assertThat(exception.getMessage()).isEqualTo("Nazwa użytkownika '" + newUsername + "' jest już zajęta przez innego użytkownika."); // Zaktualizowano komunikat
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).existsByUsername(newUsername);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Powinien pozwolić na aktualizację użytkownika jego własną, istniejącą nazwą użytkownika") // Przetłumaczono
    void updateUser_shouldAllowUpdateWithOwnExistingUsername() {
        // Arrange
        Long userId = 1L;
        String currentUsername = "testUser1";
        User existingUser = new User(currentUsername);
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        // `existsByUsername` nie powinno być wołane, jeśli username się nie zmienia,
        // lub jeśli jest wołane, to warunek `!userToUpdate.getUsername().equals(newUsername)`
        // w serwisie zapobiegnie rzuceniu wyjątku.
        // when(userRepository.existsByUsername(currentUsername)).thenReturn(true); // To jest prawda
        when(userRepository.save(any(User.class))).thenReturn(existingUser); // Zwraca tego samego użytkownika

        // Act
        User result = userService.updateUser(userId, currentUsername);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(currentUsername);
        verify(userRepository, times(1)).findById(userId);
        // Kluczowe: sprawdzanie duplikatu dla własnego username nie powinno blokować operacji.
        // W obecnej implementacji serwisu, `existsByUsername` nie jest wołane, jeśli nazwa się nie zmienia.
        verify(userRepository, never()).existsByUsername(currentUsername);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    @DisplayName("Powinien usunąć użytkownika, gdy użytkownik istnieje") // Przetłumaczono
    void deleteUser_shouldDeleteUser_whenUserExists() {
        // Arrange
        Long userId = 1L;
        // Symulacja istnienia użytkownika i jego pobrania
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        // Mockowanie metody void delete(User)
        doNothing().when(userRepository).delete(user1);

        // Act
        assertDoesNotThrow(() -> { // Sprawdzamy, czy nie rzuca wyjątku
            userService.deleteUser(userId);
        });

        // Assert
        verify(userRepository, times(1)).findById(userId); // Sprawdza, czy użytkownik został pobrany
        verify(userRepository, times(1)).delete(user1);   // Sprawdza, czy metoda delete została wywołana na obiekcie
    }

    @Test
    @DisplayName("Powinien rzucić ResourceNotFoundException podczas usuwania nieistniejącego użytkownika") // Przetłumaczono
    void deleteUser_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {
        // Arrange
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty()); // Użytkownik nie istnieje

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(userId);
        });
        assertThat(exception.getMessage()).isEqualTo("Nie znaleziono użytkownika o ID: " + userId + " do usunięcia."); // Zaktualizowano komunikat
        verify(userRepository, times(1)).findById(userId); // findById jest wołane
        verify(userRepository, never()).deleteById(anyLong()); // deleteById nie powinno być wywołane
        verify(userRepository, never()).delete(any(User.class)); // delete(User) nie powinno być wywołane
    }
}