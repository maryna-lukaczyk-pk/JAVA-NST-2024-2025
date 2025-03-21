package s.kopec.ProjectManagerApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import s.kopec.ProjectManagerApp.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {}
