package org.example.projectmanager.repository;

import org.example.projectmanager.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProjectRepository extends JpaRepository<Project, Long> {}