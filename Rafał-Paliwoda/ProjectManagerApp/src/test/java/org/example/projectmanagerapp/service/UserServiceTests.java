package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTests {
    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        projectRepository = Mockito.mock(ProjectRepository.class);
        userService = new UserService(userRepository, projectRepository);
    }

    @Test
    @DisplayName("It should create a new user")
    void createUser() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("It should get all users")
    void getAllUsers() {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("It should get user by ID")
    void getUserById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("It should return null when user not found")
    void getUserByIdNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User result = userService.getUserById(userId);

        assertNull(result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("It should update an existing user")
    void updateUser() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldusername");

        User userDetails = new User();
        userDetails.setUsername("newusername");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User result = userService.updateUser(userId, userDetails);

        assertNotNull(result);
        assertEquals("newusername", result.getUsername());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    @DisplayName("It should throw exception when updating non-existent user")
    void updateUserNotFound() {
        Long userId = 1L;
        User userDetails = new User();
        userDetails.setUsername("newusername");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.updateUser(userId, userDetails);
        });

        assertEquals("404 NOT_FOUND \"Użytkownik o ID 1 nie został znaleziony\"", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("It should delete an existing user")
    void deleteUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("It should throw exception when deleting non-existent user")
    void deleteUserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.deleteUser(userId);
        });

        assertEquals("404 NOT_FOUND \"Użytkownik o ID 1 nie został znaleziony\"", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    @DisplayName("It should assign user to project")
    void assignUserToProject() {
        Long userId = 1L;
        Long projectId = 2L;

        User user = new User();
        user.setId(userId);

        Project project = new Project();
        project.setId(projectId);
        project.setUsers(new ArrayList<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        userService.assignUserToProject(userId, projectId);

        assertTrue(project.getUsers().contains(user));
        verify(userRepository, times(1)).findById(userId);
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("It should assign user to project with null users list")
    void assignUserToProjectWithNullUsersList() {
        Long userId = 1L;
        Long projectId = 2L;

        User user = new User();
        user.setId(userId);

        Project project = new Project();
        project.setId(projectId);
        project.setUsers(null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        userService.assignUserToProject(userId, projectId);

        assertNotNull(project.getUsers());
        assertTrue(project.getUsers().contains(user));
        verify(userRepository, times(1)).findById(userId);
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("It should not assign user if already assigned to project")
    void assignUserToProjectAlreadyAssigned() {
        Long userId = 1L;
        Long projectId = 2L;

        User user = new User();
        user.setId(userId);

        Project project = new Project();
        project.setId(projectId);
        List<User> users = new ArrayList<>();
        users.add(user);
        project.setUsers(users);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        userService.assignUserToProject(userId, projectId);

        assertEquals(1, project.getUsers().size());
        verify(userRepository, times(1)).findById(userId);
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("It should throw exception when assigning non-existent user to project")
    void assignNonExistentUserToProject() {
        Long userId = 1L;
        Long projectId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.assignUserToProject(userId, projectId);
        });

        assertEquals("404 NOT_FOUND \"Użytkownik o ID 1 nie został znaleziony\"", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(projectRepository, never()).findById(any());
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("It should throw exception when assigning user to non-existent project")
    void assignUserToNonExistentProject() {
        Long userId = 1L;
        Long projectId = 2L;

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.assignUserToProject(userId, projectId);
        });

        assertEquals("404 NOT_FOUND \"Projekt o ID 2 nie został znaleziony\"", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("It should remove user from project")
    void removeUserFromProject() {
        Long userId = 1L;
        Long projectId = 2L;

        User user = new User();
        user.setId(userId);

        Project project = new Project();
        project.setId(projectId);
        List<User> users = new ArrayList<>();
        users.add(user);
        project.setUsers(users);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        userService.removeUserFromProject(userId, projectId);

        assertFalse(project.getUsers().contains(user));
        verify(userRepository, times(1)).findById(userId);
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("It should not fail when removing user not in project")
    void removeUserFromProjectNotAssigned() {
        Long userId = 1L;
        Long projectId = 2L;

        User user = new User();
        user.setId(userId);

        Project project = new Project();
        project.setId(projectId);
        project.setUsers(new ArrayList<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        userService.removeUserFromProject(userId, projectId);

        assertFalse(project.getUsers().contains(user));
        verify(userRepository, times(1)).findById(userId);
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("It should handle removing user from project with null users list")
    void removeUserFromProjectWithNullUsersList() {
        Long userId = 1L;
        Long projectId = 2L;

        User user = new User();
        user.setId(userId);

        Project project = new Project();
        project.setId(projectId);
        project.setUsers(null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        userService.removeUserFromProject(userId, projectId);

        verify(userRepository, times(1)).findById(userId);
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("It should throw exception when removing non-existent user from project")
    void removeNonExistentUserFromProject() {
        Long userId = 1L;
        Long projectId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.removeUserFromProject(userId, projectId);
        });

        assertEquals("404 NOT_FOUND \"Użytkownik o ID 1 nie został znaleziony\"", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(projectRepository, never()).findById(any());
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("It should throw exception when removing user from non-existent project")
    void removeUserFromNonExistentProject() {
        Long userId = 1L;
        Long projectId = 2L;

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.removeUserFromProject(userId, projectId);
        });

        assertEquals("404 NOT_FOUND \"Projekt o ID 2 nie został znaleziony\"", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, never()).save(any(Project.class));
    }
}