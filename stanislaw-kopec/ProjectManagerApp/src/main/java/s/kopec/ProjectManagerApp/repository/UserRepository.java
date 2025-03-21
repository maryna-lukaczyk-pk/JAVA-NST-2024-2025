package s.kopec.ProjectManagerApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import s.kopec.ProjectManagerApp.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {}
