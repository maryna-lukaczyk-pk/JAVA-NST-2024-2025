package com.example.projectmanagerapp.repository;

import com.example.projectmanagerapp.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public class ProjectRepository extends JpaRepository<Project, Long> {
}
