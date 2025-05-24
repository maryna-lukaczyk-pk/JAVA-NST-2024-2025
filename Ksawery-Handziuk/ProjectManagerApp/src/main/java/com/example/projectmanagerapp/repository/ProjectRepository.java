// src/main/java/com/example/projectmanagerapp/repository/ProjectRepository.java
package com.example.projectmanagerapp.repository;

import com.example.projectmanagerapp.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Dodatkowa metoda do sprawdzania istnienia projektu po nazwie,
    // przydatna przy tworzeniu i aktualizacji, aby zapewnić unikalność nazw.
    boolean existsByName(String name);
}