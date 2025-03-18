package com.example.projectmanagerapp.repository;

import com.example.projectmanagerapp.entity.projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface projects_repository extends JpaRepository <projects, Long> {
}