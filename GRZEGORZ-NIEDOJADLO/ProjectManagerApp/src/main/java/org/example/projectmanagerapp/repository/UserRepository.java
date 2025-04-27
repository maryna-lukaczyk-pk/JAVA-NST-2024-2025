package org.example.projectmanagerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.projectmanagerapp.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
