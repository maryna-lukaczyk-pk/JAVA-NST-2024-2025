// src/main/java/com/example/projectmanagerapp/repository/TaskRepository.java
package com.example.projectmanagerapp.repository;

import com.example.projectmanagerapp.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Tutaj można dodać niestandardowe metody zapytań, jeśli są potrzebne,
    // np. findByProjectId(Long projectId) jeśli Task byłby powiązany z Project.
}