package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.interfaces.ProjectResponseDTO;
import org.example.projectmanagerapp.interfaces.UserResponseDTO;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectRepository projectRepository;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        projectRepository = Mockito.mock(ProjectRepository.class);
        userService = new UserService(userRepository, projectRepository);
    }

    @Test
    void getUserById_returnsUserResponseDTO_whenUserExists() {
        User user = new User("testuser");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO dto = userService.getUserById(1L);
        assertEquals(1L, dto.getId());
        assertEquals("testuser", dto.getUsername());
    }

    @Test
    void getUserById_throwsException_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> userService.getUserById(1L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void registerUser_savesAndReturnsUserResponseDTO() {
        User user = new User("newuser");
        user.setId(2L);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO dto = userService.registerUser("newuser");
        assertEquals(2L, dto.getId());
        assertEquals("newuser", dto.getUsername());
    }

    @Test
    void loginUser_returnsUserResponseDTO_whenUserExists() {
        User user = new User("loginuser");
        user.setId(3L);
        when(userRepository.findByUsername("loginuser")).thenReturn(Optional.of(user));

        UserResponseDTO dto = userService.loginUser("loginuser");
        assertEquals(3L, dto.getId());
        assertEquals("loginuser", dto.getUsername());
    }

    @Test
    void loginUser_throwsException_whenUserNotFound() {
        when(userRepository.findByUsername("nouser")).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> userService.loginUser("nouser"));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void deleteUser_deletesUser_whenUserExists() {
        User user = new User("todelete");
        user.setId(4L);
        when(userRepository.findById(4L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);
        userService.deleteUser(4L);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_throwsException_whenUserNotFound() {
        when(userRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> userService.deleteUser(5L));
    }

    @Test
    void getUserProjects_returnsProjectResponseDTOList() {
        User user = new User("projuser");
        user.setId(6L);
        Project project1 = new Project("Project1");
        project1.setId(10L);
        Project project2 = new Project("Project2");
        project2.setId(11L);
        Set<Project> projects = new HashSet<>(Arrays.asList(project1, project2));
        user.setProjects(projects);
        when(userRepository.findById(6L)).thenReturn(Optional.of(user));

        List<ProjectResponseDTO> dtos = userService.getUserProjects(6L);
        assertEquals(2, dtos.size());
        assertTrue(dtos.stream().anyMatch(dto -> dto.getId().equals(10L)));
        assertTrue(dtos.stream().anyMatch(dto -> dto.getId().equals(11L)));
    }

    @Test
    void associateUserWithProject_throwsException_whenUserNotFound() {
        when(userRepository.findById(8L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> userService.associateUserWithProject(8L, 21L));
    }

    @Test
    void associateUserWithProject_throwsException_whenProjectNotFound() {
        User user = new User("assocuser2");
        user.setId(9L);
        when(userRepository.findById(9L)).thenReturn(Optional.of(user));
        when(projectRepository.findById(22L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> userService.associateUserWithProject(9L, 22L));
    }

    @Test
    void removeUserFromProject_throwsException_whenUserNotFound() {
        when(userRepository.findById(11L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> userService.removeUserFromProject(11L, 31L));
    }

    @Test
    void removeUserFromProject_throwsException_whenProjectNotFound() {
        User user = new User("removeuser2");
        user.setId(12L);
        when(userRepository.findById(12L)).thenReturn(Optional.of(user));
        when(projectRepository.findById(32L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> userService.removeUserFromProject(12L, 32L));
    }
}
