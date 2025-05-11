package org.jerzy.projectmanagerapp.service;

import org.jerzy.projectmanagerapp.entity.User;
import org.jerzy.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

  private UserRepository userRepository;
  private UserService userService;

  @BeforeEach
  void setUp() {
    userRepository = Mockito.mock(UserRepository.class);
    userService = new UserService(userRepository);
  }

  @Test
  void testGetAllUsers() {
    List<User> users = List.of(new User(), new User());
    when(userRepository.findAll()).thenReturn(users);

    List<User> result = userService.getAllUsers();
    assertEquals(2, result.size());
    verify(userRepository).findAll();
  }

  @Test
  void testGetById_validId() {
    User user = new User();
    user.setId(1L);
    when(userRepository.findById(1)).thenReturn(Optional.of(user));

    User result = userService.getById("1");
    assertEquals(1L, result.getId());
  }

  @Test
  void testGetById_invalidIdFormat() {
    assertThrows(IllegalArgumentException.class, () -> userService.getById("abc"));
  }

  @Test
  void testCreateUser() {
    User user = new User();
    when(userRepository.save(user)).thenReturn(user);

    User result = userService.create(user);
    assertEquals(user, result);
  }

  @Test
  void testUpdate_validId() {
    User existing = new User();
    existing.setId(1L);
    existing.setUsername("old");

    User updated = new User();
    updated.setUsername("new");

    when(userRepository.findById(1)).thenReturn(Optional.of(existing));
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    User result = userService.update("1", updated);
    assertEquals("new", result.getUsername());
  }

  @Test
  void testUpdate_invalidIdFormat() {
    assertThrows(IllegalArgumentException.class, () -> userService.update("xyz", new User()));
  }

  @Test
  void testDelete_validId() {
    when(userRepository.existsById(1)).thenReturn(true);

    userService.delete("1");

    verify(userRepository).deleteById(1);
  }

  @Test
  void testDelete_nonExistingId() {
    when(userRepository.existsById(5)).thenReturn(false);

    assertThrows(IllegalArgumentException.class, () -> userService.delete("5"));
  }

  @Test
  void testDelete_invalidIdFormat() {
    assertThrows(IllegalArgumentException.class, () -> userService.delete("bad_id"));
  }
}
