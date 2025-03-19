package org.example.projectmanager.repository;

import org.example.projectmanager.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {}
public interface UsersRepository extends JpaRepository<Users, Long> {}
public interface TasksRepository extends JpaRepository<Tasks, Long> {}