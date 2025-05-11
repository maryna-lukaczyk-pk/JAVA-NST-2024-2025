package org.jerzy.projectmanagerapp.controller;

import org.jerzy.projectmanagerapp.entity.User;
import org.jerzy.projectmanagerapp.repository.UserRepository;
import org.jerzy.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

  private UserRepository userRepository;
  private UserService userService;
  private UserController userController;



  @BeforeEach
  void setUp() {
    userRepository = Mockito.mock(UserRepository.class);
    userService = new UserService(userRepository);
    userController = new UserController(userRepository);
  }

  @Test
  void testGetAllUsers() {
    when(userService.getAllUsers()).thenReturn(List.of(new User(), new User()));

    List<User> result = userController.get();
    assertEquals(2, result.size());
  }

  @Test
  void testGetUserById() {
    User user = new User();
    user.setId(1L);
    when(userService.getById("1")).thenReturn(user);

    User result = userController.getUserById("1");
    assertEquals(1L, result.getId());
  }

  @Test
  void testPost() {
    User user = new User();
    user.setUsername("new_user");
    when(userService.create(any())).thenReturn(user);

    ResponseEntity<User> response = userController.postMethodName(user);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals("new_user", response.getBody().getUsername());
  }

  @Test
  void testUpdateUser_valid() {
    User user = new User();
    user.setUsername("updated");
    when(userService.update("1", user)).thenReturn(user);

    ResponseEntity<User> response = userController.updateUser("1", user);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("updated", response.getBody().getUsername());
  }

  @Test
  void testUpdateUser_invalid() {
    when(userService.update(eq("1"), any())).thenThrow(new IllegalArgumentException());

    ResponseEntity<User> response = userController.updateUser("1", new User());
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testDeleteUser_valid() {
    ResponseEntity<Void> response = userController.deleteUser("1");
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(userService).delete("1");
  }

  @Test
  void testDeleteUser_invalid() {
    doThrow(new IllegalArgumentException()).when(userService).delete("1");

    ResponseEntity<Void> response = userController.deleteUser("1");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
