package com.example.demo.repository;

import com.example.demo.entity.Project;
import com.example.demo.entity.ProjectUsers;
import com.example.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectUsersRepository extends JpaRepository<ProjectUsers, Long> {
    List<ProjectUsers> findByProject(Project project);
    List<ProjectUsers> findByUser(Users user);
    Optional<ProjectUsers> findByProjectAndUser(Project project, Users user);
}
