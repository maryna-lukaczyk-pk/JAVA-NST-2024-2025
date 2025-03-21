package s.kopec.ProjectManagerApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import s.kopec.ProjectManagerApp.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {}
