package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// Interfejs repozytorium dla encji użytkownika
public interface UserRepository extends JpaRepository<User, Long> {
}