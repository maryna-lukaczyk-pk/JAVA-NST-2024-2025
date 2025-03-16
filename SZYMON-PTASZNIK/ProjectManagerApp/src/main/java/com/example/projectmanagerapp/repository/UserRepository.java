package com.example.projectmanagerapp.repository;

import com.example.projectmanagerapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public class UserRepository extends JpaRepository<User, Long> {
}
