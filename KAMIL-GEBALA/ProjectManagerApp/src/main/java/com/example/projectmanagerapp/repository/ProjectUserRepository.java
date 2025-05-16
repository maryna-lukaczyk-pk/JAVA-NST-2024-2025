package com.example.projectmanagerapp.repository;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.ProjectUser;
import com.example.projectmanagerapp.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, ProjectUser.ProjectUserId> {
    List<ProjectUser> findByProject(Project project);
    List<ProjectUser> findByUser(Users user);
    ProjectUser findByProjectAndUser(Project project, Users user);
}
