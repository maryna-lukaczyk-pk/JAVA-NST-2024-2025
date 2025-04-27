package com.example.projectmanagerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.projectmanagerapp.entity.Projects;

@Repository
public interface ProjectRepository extends JpaRepository<Projects, Long> {

}
