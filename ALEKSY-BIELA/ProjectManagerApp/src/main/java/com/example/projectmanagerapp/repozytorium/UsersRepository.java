package com.example.projectmanagerapp.repozytorium;

import com.example.projectmanagerapp.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
