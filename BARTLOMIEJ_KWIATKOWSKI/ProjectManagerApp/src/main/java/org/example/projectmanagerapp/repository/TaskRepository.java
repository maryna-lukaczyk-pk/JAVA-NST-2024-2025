package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Tasks, Long> {}