package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.interfaces.ProjectResponseDTO;
import org.example.projectmanagerapp.interfaces.ProjectTasksResponseDTO;
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

public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {

        userRepository = Mockito.mock(UserRepository.class);
        projectRepository = Mockito.mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository, userRepository);
    }

    @Test
    void getAllProjects_returnsListOfProjectResponseDTO() {
        Project project1 = new Project("Project1");
        project1.setId(1L);
        Project project2 = new Project("Project2");
        project2.setId(2L);
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<ProjectResponseDTO> result = projectService.getAllProjects();
        assertEquals(2, result.size());
        assertEquals("Project1", result.get(0).getName());
        assertEquals("Project2", result.get(1).getName());
    }

    @Test
    void getProjectById_returnsProjectResponseDTO_whenProjectExists() {
        Project project = new Project("TestProject");
        project.setId(10L);
        when(projectRepository.findById(10L)).thenReturn(Optional.of(project));

        ProjectResponseDTO dto = projectService.getProjectById(10L);
        assertEquals(10L, dto.getId());
        assertEquals("TestProject", dto.getName());
    }

    @Test
    void getProjectById_throwsException_whenProjectNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> projectService.getProjectById(99L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void getAssociatedUsers_returnsUserResponseDTOList() {
        Project project = new Project("UserProject");
        project.setId(20L);
        User user1 = new User("user1");
        user1.setId(1L);
        User user2 = new User("user2");
        user2.setId(2L);
        Set<User> users = new HashSet<>(Arrays.asList(user1, user2));
        project.setUsers(users);
        when(projectRepository.findById(20L)).thenReturn(Optional.of(project));

        List<UserResponseDTO> result = projectService.getAssociatedUsers(20L);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.getId().equals(1L)));
        assertTrue(result.stream().anyMatch(dto -> dto.getId().equals(2L)));
    }

    @Test
    void getAssociatedUsers_throwsException_whenProjectNotFound() {
        when(projectRepository.findById(21L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> projectService.getAssociatedUsers(21L));
    }

    @Test
    void getAssociatedTasks_returnsProjectTasksResponseDTOList() {
        Project project = new Project("TaskProject");
        project.setId(30L);
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task1");
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task2");
        Set<Task> tasks = new HashSet<>(Arrays.asList(task1, task2));
        project.setTasks(tasks);
        when(projectRepository.findById(30L)).thenReturn(Optional.of(project));

        List<ProjectTasksResponseDTO> result = projectService.getAssociatedTasks(30L);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.getId().equals(1L)));
        assertTrue(result.stream().anyMatch(dto -> dto.getId().equals(2L)));
    }

    @Test
    void getAssociatedTasks_throwsException_whenProjectNotFound() {
        when(projectRepository.findById(31L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> projectService.getAssociatedTasks(31L));
    }

    @Test
    void createProject_savesAndReturnsProjectResponseDTO() {
        Project project = new Project("NewProject");
        project.setId(40L);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponseDTO dto = projectService.createProject("NewProject");
        assertEquals(40L, dto.getId());
        assertEquals("NewProject", dto.getName());
    }

    @Test
    void renameProject_updatesAndReturnsProjectResponseDTO() {
        Project project = new Project("OldName");
        project.setId(50L);
        when(projectRepository.findById(50L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponseDTO dto = projectService.renameProject(50L, "NewName");
        assertEquals(50L, dto.getId());
        assertEquals("NewName", dto.getName());
    }

    @Test
    void renameProject_throwsException_whenProjectNotFound() {
        when(projectRepository.findById(51L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> projectService.renameProject(51L, "DoesNotExist"));
    }

    @Test
    void deleteProject_deletesProject_whenProjectExists() {
        Project project = new Project("ToDelete");
        project.setId(60L);
        when(projectRepository.findById(60L)).thenReturn(Optional.of(project));
        doNothing().when(projectRepository).delete(project);
        projectService.deleteProject(60L);
        verify(projectRepository).delete(project);
    }

    @Test
    void deleteProject_throwsException_whenProjectNotFound() {
        when(projectRepository.findById(61L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> projectService.deleteProject(61L));
    }

    @Test
    void associateUserWithProject_throwsException_whenProjectNotFound() {
        when(projectRepository.findById(71L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> projectService.associateUserWithProject(8L, 71L));
    }

    @Test
    void associateUserWithProject_throwsException_whenUserNotFound() {
        Project project = new Project("AssocProject2");
        project.setId(72L);
        when(projectRepository.findById(72L)).thenReturn(Optional.of(project));
        when(userRepository.findById(9L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> projectService.associateUserWithProject(9L, 72L));
    }

    @Test
    void removeUserFromProject_throwsException_whenProjectNotFound() {
        when(projectRepository.findById(81L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> projectService.removeUserFromProject(10L, 81L));
    }

    @Test
    void removeUserFromProject_throwsException_whenUserNotFound() {
        Project project = new Project("RemoveProject2");
        project.setId(82L);
        when(projectRepository.findById(82L)).thenReturn(Optional.of(project));
        when(userRepository.findById(11L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> projectService.removeUserFromProject(11L, 82L));
    }
}
