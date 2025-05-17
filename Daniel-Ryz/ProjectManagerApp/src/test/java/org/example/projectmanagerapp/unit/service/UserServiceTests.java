package org.example.projectmanagerapp.unit.service;

import org.example.projectmanagerapp.dto.CreateUserRequest;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.UserRepository;
import org.example.projectmanagerapp.service.ProjectService;
import org.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    private UserRepository userRepository;
    private ProjectService projectService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        projectService = Mockito.mock(ProjectService.class);
        userService = new UserService(userRepository, projectService);
    }

    @Test
    @DisplayName("Should return all users")
    void testGetAllUsers() {
        Users user1 = new Users();
        user1.setUsername("User1");

        Users user2 = new Users();
        user2.setUsername("User2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<Users> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return user by ID")
    void testGetUserById() {
        Users user = new Users();
        user.setUsername("TestUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Users result = userService.getUserById(1L);

        assertEquals("TestUser", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw when user not found")
    void testGetUserByIdThrows() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
    }

    @Test
    @DisplayName("Should create a new user")
    void testCreateUser() {
        CreateUserRequest request = new CreateUserRequest("NewUser");

        Users saved = new Users();
        saved.setUsername("NewUser");

        when(userRepository.save(any(Users.class))).thenReturn(saved);

        Users result = userService.createUser(request);

        assertEquals("NewUser", result.getUsername());
        verify(userRepository, times(1)).save(any(Users.class));
    }

    @Test
    @DisplayName("Should update existing user")
    void testUpdateUser() {
        Users existing = new Users();
        existing.setId(1L);
        existing.setUsername("OldUser");

        CreateUserRequest request = new CreateUserRequest("UpdatedUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        Users result = userService.updateUser(1L, request);

        assertEquals("UpdatedUser", result.getUsername());
        verify(userRepository, times(1)).save(existing);
    }

    @Test
    @DisplayName("Should delete user by ID")
    void testDeleteUser() {
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should assign project to user")
    void testAssignProjectToUser() {
        Users user = new Users();
        user.setId(1L);

        Project project = new Project();
        project.setId(10L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectService.getProjectById(10L)).thenReturn(project);

        userService.assignProjectToUser(10L, 1L);

        assertTrue(user.getProjects().contains(project));
        verify(userRepository, times(1)).save(user);
    }
}

