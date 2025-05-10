package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.tasks.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Tasks, Long> {
}