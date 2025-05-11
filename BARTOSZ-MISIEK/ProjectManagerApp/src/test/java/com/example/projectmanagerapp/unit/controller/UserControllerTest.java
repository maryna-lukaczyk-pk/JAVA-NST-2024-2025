package com.example.projectmanagerapp.unit.controller;

import org.example.projectmanager.controller.UserController;
import org.example.projectmanager.entity.user.User;
import org.example.projectmanager.exception.EntityNotFoundException;
import org.example.projectmanager.repository.UserRepository;
import org.example.projectmanager.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class UserControllerTest {
    private UserRepository userRepository;
    private UserController userController;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository);
        userController = new UserController(userService);
    }

    @Test
    public void Get_ShouldReturn_User() throws EntityNotFoundException {
        var userId = 1L;
        var user = new User();
        user.setId(userId);
        user.setUsername("User A");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        var result = userController.get(userId);

        Assertions.assertEquals(user.getId(), result.id);
        Assertions.assertEquals(user.getUsername(), result.username);
    }

    @Test
    public void Get_ShouldThrow_EntityNotFoundException() {
        var userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> userController.get(userId));
    }

    @Test
    public void Create_ShouldReturn_CreatedUserId() {
        var user = new User();
        user.setId(1L);
        user.setUsername("User A");

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        var dto = new org.example.projectmanager.dto.user.UserCreateDto();
        dto.username = "User A";

        var result = userController.create(dto);

        Assertions.assertEquals(user.getId(), result);
    }

    @Test
    public void Delete_ShouldDelete_User() throws EntityNotFoundException {
        var userId = 1L;

        Mockito.when(userRepository.existsById(userId)).thenReturn(true);

        userController.delete(userId);

        Mockito.verify(userRepository).deleteById(userId);
    }

    @Test
    public void Delete_ShouldThrow_EntityNotFoundException() {
        var userId = 1L;

        Mockito.when(userRepository.existsById(userId)).thenReturn(false);

        Assertions.assertThrows(EntityNotFoundException.class, () -> userController.delete(userId));
    }

    @Test
    public void Update_ShouldUpdate_User() throws EntityNotFoundException {
        var userId = 1L;
        var user = new User();
        user.setId(userId);
        user.setUsername("User A");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        var dto = new org.example.projectmanager.dto.user.UserEditDto();
        dto.id = userId;
        dto.username = "User B";

        userController.update(dto);

        Assertions.assertEquals("User B", user.getUsername());
    }

    @Test
    public void Update_ShouldThrow_EntityNotFoundException() {
        var userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        var dto = new org.example.projectmanager.dto.user.UserEditDto();
        dto.id = userId;

        Assertions.assertThrows(EntityNotFoundException.class, () -> userController.update(dto));
    }
}