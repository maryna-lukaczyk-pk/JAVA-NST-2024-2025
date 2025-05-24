// src/main/java/com/example/projectmanagerapp/service/UserService.java
package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project; // Potrzebne, jeśli User ma być usuwany z projektów
import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import com.example.projectmanagerapp.exception.ResourceNotFoundException;
import com.example.projectmanagerapp.exception.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
// Usunięto import Optional, ponieważ findById zwraca Optional, ale metody serwisu
// bezpośrednio zwracają User lub rzucają wyjątek, co jest dobrą praktyką w warstwie serwisu.

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Metoda do wyszukiwania obiektu (użytkownika) po jego ID
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono użytkownika o ID: " + id));
    }

    @Transactional
    public User createUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwa użytkownika nie może być pusta.");
        }
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Nazwa użytkownika '" + username + "' już istnieje.");
        }
        User newUser = new User(username);
        return userRepository.save(newUser);
    }

    // Metoda do aktualizacji danych obiektu (użytkownika) po jego ID
    @Transactional
    public User updateUser(Long id, String newUsername) { // Przyjmuje ID i nowe dane
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono użytkownika o ID: " + id + " do aktualizacji."));

        if (newUsername == null || newUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Nowa nazwa użytkownika nie może być pusta.");
        }

        // Opcjonalnie: sprawdź, czy nowy username nie jest już zajęty przez INNEGO użytkownika
        if (!userToUpdate.getUsername().equals(newUsername) && userRepository.existsByUsername(newUsername)) {
            throw new DuplicateResourceException("Nazwa użytkownika '" + newUsername + "' jest już zajęta przez innego użytkownika.");
        }

        userToUpdate.setUsername(newUsername);
        return userRepository.save(userToUpdate); // Zapisz zaktualizowanego użytkownika
    }

    // Metoda do usuwania obiektu (użytkownika) po jego ID
    @Transactional
    public void deleteUser(Long id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono użytkownika o ID: " + id + " do usunięcia."));

        // Przed usunięciem użytkownika, należy rozważyć, co zrobić z jego powiązaniami
        // np. z projektami. Jeśli użytkownik jest usunięty, jego powiązania w tabeli
        // project_app_user powinny również zostać usunięte.
        // JPA może to obsłużyć automatycznie, jeśli relacja jest odpowiednio skonfigurowana
        // (np. User jest właścicielem relacji lub Project ma CascadeType.REMOVE na kolekcji users).
        // Alternatywnie, można to zrobić ręcznie:
        for (Project project : userToDelete.getProjects()) {
            project.getUsers().remove(userToDelete);
            // Można by zapisać projekt, jeśli repozytorium projektu jest dostępne: projectRepository.save(project);
            // Jednak lepszym podejściem jest, gdy encja sama zarządza swoją stroną relacji,
            // a usunięcie encji User powoduje usunięcie wpisów w tabeli łączącej.
        }
        userToDelete.getProjects().clear(); // Wyczyść kolekcję po stronie Usera
        // userRepository.save(userToDelete); // Zapisanie użytkownika z wyczyszczoną kolekcją (jeśli konieczne przed delete)

        userRepository.delete(userToDelete); // Użyj delete(User) zamiast deleteById(id) aby wywołać callbacki JPA, jeśli są
        // lub po prostu userRepository.deleteById(id); jeśli sprawdzenie istnienia jest wystarczające.
    }
}