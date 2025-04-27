package org.example.projectmanager.repository;

import org.example.projectmanager.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TasksRepository extends JpaRepository<Tasks, Long> {}