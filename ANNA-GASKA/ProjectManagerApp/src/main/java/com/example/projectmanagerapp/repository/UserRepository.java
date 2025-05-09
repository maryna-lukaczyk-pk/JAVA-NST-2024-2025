package com.example.projectmanagerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.projectmanagerapp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}