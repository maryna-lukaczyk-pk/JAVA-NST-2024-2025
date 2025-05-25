package com.example.projectmanagerapp.repository;

import com.example.projectmanagerapp.entity.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> { }
