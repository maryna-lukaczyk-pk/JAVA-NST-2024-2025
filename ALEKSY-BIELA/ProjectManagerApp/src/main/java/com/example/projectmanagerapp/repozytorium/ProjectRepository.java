package com.example.projectmanagerapp.repozytorium;

import com.example.projectmanagerapp.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
