package com.example.projectmanagerapp.repository;

import com.example.projectmanagerapp.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Project, Long> {
}
