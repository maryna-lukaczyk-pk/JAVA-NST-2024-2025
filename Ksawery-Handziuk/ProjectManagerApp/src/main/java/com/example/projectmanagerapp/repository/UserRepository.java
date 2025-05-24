// src/main/java/com/example/projectmanagerapp/repository/UserRepository.java
package com.example.projectmanagerapp.repository;

import com.example.projectmanagerapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Metoda sprawdzająca, czy użytkownik o podanej nazwie użytkownika już istnieje.
    // Używana do walidacji unikalności nazwy użytkownika.
    boolean existsByUsername(String username);
}