package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.dto.UserDTO;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.ProjectUser;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.ProjectUserRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Testy związane z serwisem użytkownika
class UserServiceTest {

    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private ProjectUserRepository projectUserRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        projectRepository = Mockito.mock(ProjectRepository.class);
        projectUserRepository = Mockito.mock(ProjectUserRepository.class);
        userService = new UserService(userRepository, projectRepository, projectUserRepository);
    }

    @Test
    @DisplayName("Should return all users")
    void testGetAllUsers() {
        User u1 = new User("TestUser1");
        u1.setId(1L);
        User u2 = new User("TestUser2");
        u2.setId(2L);
        when(userRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<UserDTO> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("TestUser1", users.get(0).username());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return user by ID")
    void testGetUserById_Found() {
        User u = new User("FoundUser");
        u.setId(42L);
        when(userRepository.findById(42L)).thenReturn(Optional.of(u));

        UserDTO dto = userService.getUserById(42L);

        assertEquals(42L, dto.id());
        assertEquals("FoundUser", dto.username());
        verify(userRepository).findById(42L);
    }

    @Test
    @DisplayName("getUserById throws 404 when not found")
    void testGetUserById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.getUserById(99L));
        verify(userRepository).findById(99L);
    }

    @Test
    @DisplayName("Should create user")
    void testCreateUser() {
        User saved = new User("NewUser");
        saved.setId(5L);
        when(userRepository.save(any(User.class))).thenReturn(saved);

        UserDTO dto = userService.createUser(new User("Ignored"));

        assertEquals(5L, dto.id());
        assertEquals("NewUser", dto.username());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should delete user or throw if missing")
    void testDeleteUser() {
        when(userRepository.existsById(10L)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(10L));
        verify(userRepository).deleteById(10L);

        when(userRepository.existsById(11L)).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> userService.deleteUser(11L));
    }

    @Test
    @DisplayName("Should assign user to project")
    void testAssignUserToProject() {
        User u = new User("U");
        u.setId(1L);
        Project p = new Project("P");
        p.setId(2L);
        p.setProjectUsers(new HashSet<>());
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));
        when(projectRepository.findById(2L)).thenReturn(Optional.of(p));
        when(projectUserRepository.save(any(ProjectUser.class))).thenAnswer(i -> i.getArgument(0));

        UserDTO dto = userService.assignUserToProject(1L, 2L);

        assertTrue(dto.project_id().contains(2L));
        verify(projectUserRepository).save(any(ProjectUser.class));
    }
}