package org.jerzy.projectmanagerapp.service;

import java.util.List;

import org.jerzy.projectmanagerapp.entity.User;
import org.jerzy.projectmanagerapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository repository) {
    this.userRepository = repository;
  }

  public List<User> getAllUsers() {
    return this.userRepository.findAll();
  }

  public User getById(String id) {
    int userId = 0;
    try {
      userId = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid id: " + e);
    }
    return this.userRepository.findById((long) userId).get();
  }

  public User create(User user) {
    return this.userRepository.save(user);
  }

  public User update(String id, User user) {
    int userId;
    try {
      userId = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid user ID: " + e);
    }

    return this.userRepository.findById((long) userId).map(existingUser -> {
      existingUser.setUsername(user.getUsername());
      existingUser.setProjects(user.getProjects());
      return this.userRepository.save(existingUser);
    }).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
  }

  public void delete(String id) {
    int userId;
    try {
      userId = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid user ID: " + id);
    }

    if (!this.userRepository.existsById((long) userId)) {
      throw new IllegalArgumentException("User not found with ID: " + id);
    }

    this.userRepository.deleteById((long) userId);
  }
}
