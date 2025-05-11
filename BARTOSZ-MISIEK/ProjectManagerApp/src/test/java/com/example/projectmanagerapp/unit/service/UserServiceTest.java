package com.example.projectmanagerapp.unit.service;

import org.example.projectmanager.dto.user.UserCreateDto;
import org.example.projectmanager.dto.user.UserDto;
import org.example.projectmanager.dto.user.UserEditDto;
import org.example.projectmanager.entity.user.User;
import org.example.projectmanager.exception.EntityNotFoundException;
import org.example.projectmanager.repository.UserRepository;
import org.example.projectmanager.service.IUserService;
import org.example.projectmanager.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest {
    private UserRepository repository;
    private IUserService service;

    @BeforeEach
    public void setUp() {
        repository = Mockito.mock(UserRepository.class);
        service = new UserService(repository);
    }

    @Test
    void FindAll_ShouldReturn_AllUsers() {
        var user = new User();
        user.setId(1L);
        user.setUsername("John Doe");

        var expected = UserDto.MapFromEntity(user);
        Mockito.when(repository.findAll()).thenReturn(List.of(user));

        var result = service.findAll();

        Assertions.assertEquals(List.of(expected), result);
    }

    @Test
    void Create_ShouldReturn_CreatedUserId() {
        var user = new User();
        user.setId(1L);
        user.setUsername("John Doe");
        Mockito.when(repository.save(Mockito.any(User.class))).thenReturn(user);

        var dto = new UserCreateDto();
        dto.username = "John Doe";

        var result = service.create(dto);

        Assertions.assertEquals(user.getId(), result);
    }

    @Test
    void Delete_ShouldDelete_User() throws EntityNotFoundException {
        var user = new User();
        user.setId(1L);
        Mockito.when(repository.existsById(user.getId())).thenReturn(true);

        service.delete(user.getId());

        Mockito.verify(repository).deleteById(user.getId());
    }

    @Test
    void Delete_ShouldThrow_EntityNotFoundException() {
        var user = new User();
        user.setId(1L);
        Mockito.when(repository.existsById(user.getId())).thenReturn(false);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.delete(user.getId()));
    }

    @Test
    void Edit_ShouldUpdate_User() throws EntityNotFoundException {
        var user = new User();
        user.setId(1L);
        user.setUsername("John Doe");
        Mockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));

        var dto = new UserEditDto();
        dto.id = 1L;
        dto.username = "Jane Doe";

        service.edit(dto);

        Assertions.assertEquals("Jane Doe", user.getUsername());
    }

    @Test
    void Edit_ShouldThrow_EntityNotFoundException() {
        var dto = new UserEditDto();
        dto.id = 1L;
        Mockito.when(repository.findById(dto.id)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.edit(dto));
    }

    @Test
    void GetById_ShouldReturn_User() throws EntityNotFoundException {
        var user = new User();
        user.setId(1L);
        user.setUsername("John Doe");
        Mockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));

        var result = service.getById(user.getId());

        Assertions.assertEquals(UserDto.MapFromEntity(user), result);
    }
}
