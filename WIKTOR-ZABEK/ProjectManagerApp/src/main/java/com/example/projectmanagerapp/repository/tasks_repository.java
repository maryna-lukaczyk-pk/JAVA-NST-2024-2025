package com.example.projectmanagerapp.repository;

import com.example.projectmanagerapp.entity.tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface tasks_repository extends JpaRepository <tasks, Long> {
}