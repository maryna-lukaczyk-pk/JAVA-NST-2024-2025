package com.example.projectmanagerapp.repozytorium;

import com.example.projectmanagerapp.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepository extends JpaRepository<Tasks, Long> {
}
