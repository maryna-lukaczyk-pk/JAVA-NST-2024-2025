package com.example.projectmanagerapp.repository;

import com.example.projectmanagerapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
