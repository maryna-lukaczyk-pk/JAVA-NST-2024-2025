package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {}