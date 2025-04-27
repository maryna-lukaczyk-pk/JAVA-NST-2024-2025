package s.kopec.ProjectManagerApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import s.kopec.ProjectManagerApp.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {}
