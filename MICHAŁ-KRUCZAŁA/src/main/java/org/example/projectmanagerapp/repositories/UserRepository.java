package org.example.projectmanagerapp.repositories;


import org.example.projectmanagerapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}