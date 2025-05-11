package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Interfejs repozytorium dla encji pośredniczącej projektu i użytkownika
@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
}