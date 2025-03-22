package org.jerzy.projectmanagerapp.repository;

import org.jerzy.projectmanagerapp.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}