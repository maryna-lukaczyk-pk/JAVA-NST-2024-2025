package org.jerzy.projectmanagerapp.repository;

import org.jerzy.projectmanagerapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}