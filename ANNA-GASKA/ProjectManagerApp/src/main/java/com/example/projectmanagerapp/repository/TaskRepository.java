package com.example.projectmanagerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.projectmanagerapp.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
