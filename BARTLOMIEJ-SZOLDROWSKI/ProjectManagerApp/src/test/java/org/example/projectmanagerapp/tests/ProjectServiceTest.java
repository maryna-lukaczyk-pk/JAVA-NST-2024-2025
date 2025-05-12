package org.example.projectmanagerapp.tests;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void getAllProjects_ShouldReturnAllProjects() {
        // Given
        Project project1 = new Project();
        project1.setName("Project 1");
        Project project2 = new Project();
        project2.setName("Project 2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        // When
        List<Project> projects = projectService.getAllProjects();

        // Then
        assertThat(projects).hasSize(2);
        assertThat(projects).extracting(Project::getName)
                .containsExactly("Project 1", "Project 2");
    }

    @Test
    void createProject_ShouldSaveAndReturnProject() {
        // Given
        Project newProject = new Project();
        newProject.setName("New Project");
        when(projectRepository.save(newProject)).thenReturn(newProject);

        // When
        Project createdProject = projectService.createProject(newProject);

        // Then
        assertThat(createdProject.getName()).isEqualTo("New Project");
        verify(projectRepository).save(newProject);
    }

    @Test
    void getProjectById_WhenExists_ShouldReturnProject() {
        // Given
        Project project = new Project();
        project.setId(1L);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        // When
        Optional<Project> foundProject = projectService.getProjectById(1L);

        // Then
        assertThat(foundProject).isPresent();
        assertThat(foundProject.get().getId()).isEqualTo(1L);
    }

    @Test
    void getProjectById_WhenNotExists_ShouldReturnEmpty() {
        // Given
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Project> foundProject = projectService.getProjectById(99L);

        // Then
        assertThat(foundProject).isEmpty();
    }

    @Test
    void updateProject_ShouldUpdateName() {
        // Given
        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("Old Name");

        Project updateData = new Project();
        updateData.setName("New Name");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(existingProject)).thenReturn(existingProject);

        // When
        Project updatedProject = projectService.updateProject(1L, updateData);

        // Then
        assertThat(updatedProject.getName()).isEqualTo("New Name");
        verify(projectRepository).save(existingProject);
    }

    @Test
    void deleteProject_WhenNoTasks_ShouldDelete() {
        // Given
        Project project = new Project();
        project.setId(1L);
        project.setTasks(Collections.emptyList()); // Brak zadań

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        doNothing().when(projectRepository).delete(project);

        // When
        projectService.deleteProject(1L);

        // Then
        verify(projectRepository).delete(project);
    }

    @Test
    void deleteProject_WhenHasTasks_ShouldThrowException() {
        // Given
        Project project = new Project();
        project.setId(1L);
        project.setTasks(List.of(new Task())); // Projekt ma przypisane zadania

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        // When & Then
        assertThatThrownBy(() -> projectService.deleteProject(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot delete project with assigned tasks");

        verify(projectRepository, never()).delete(any());
    }

    @Test
    void deleteProject_ShouldRemoveUserAssociations() {
        // Given
        Project project = new Project();
        project.setId(1L);
        project.setTasks(Collections.emptyList());

        User user = new User();
        user.getProjects().add(project);
        project.getUsers().add(user);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        doNothing().when(projectRepository).delete(project);

        // When
        projectService.deleteProject(1L);

        // Then
        assertThat(user.getProjects()).isEmpty(); // Powiązanie użytkownika usunięte
        verify(projectRepository).delete(project);
    }
}