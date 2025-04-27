package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectRepository extends JpaRepository<Projects, Long> {
}
