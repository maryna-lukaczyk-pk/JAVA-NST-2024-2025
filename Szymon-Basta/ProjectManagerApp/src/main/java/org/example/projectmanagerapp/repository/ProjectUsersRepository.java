package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.ProjectUsers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectUsersRepository extends JpaRepository<ProjectUsers, Long> {
}
