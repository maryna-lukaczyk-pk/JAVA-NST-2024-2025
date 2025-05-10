package com.example.projectmanagerapp.repository;


import com.example.projectmanagerapp.entity.users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface users_repository extends JpaRepository <users, Long> {
}