package org.example.projectmanager.repository;

import org.example.projectmanager.entity.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Department entity.
 * Provides CRUD operations and custom query methods through JpaRepository.
 */
@Repository // Indicates that this interface is a Spring Data repository.
public interface TaskRepository extends JpaRepository<Task, Long> {}
