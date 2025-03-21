package s.kopec.ProjectManagerApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import s.kopec.ProjectManagerApp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {}
