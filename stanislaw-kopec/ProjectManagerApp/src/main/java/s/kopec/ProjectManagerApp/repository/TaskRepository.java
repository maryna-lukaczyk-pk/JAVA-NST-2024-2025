package s.kopec.ProjectManagerApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import s.kopec.ProjectManagerApp.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {}
