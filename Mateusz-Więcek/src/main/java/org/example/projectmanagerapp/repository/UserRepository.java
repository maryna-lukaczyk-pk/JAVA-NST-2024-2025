package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Możemy dodać własne metody, np. wyszukiwanie użytkownika po nazwie
    User findByUsername(String username);
}
