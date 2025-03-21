package org.jerzy.projectmanagerapp.repository;

import org.jerzy.projectmanagerapp.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}