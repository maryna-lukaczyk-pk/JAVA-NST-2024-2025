package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {
    private ProjectService projectService;
    private ProjectRepository projectRepository;

    @BeforeEach
    public void setUp() {
        projectRepository = Mockito.mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    @DisplayName("Should return all projects")
    public void shouldReturnAllProjects() {
        Project project = new Project();
        project.setName("Test");

        Project project2 = new Project();
        project.setName("testProject   2");
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project, project2));
        List<Project> projects = projectService.getAllProjects();
        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("Should return 1 project")
    public void shouldReturnUserByID() {
        //given
        Project project = new Project();
        project.setName("testProject");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        Project temp = projectService.getProjectById(1L);
        assertEquals(project, temp);
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should delete project by ID")
    public void shouldDeleteUserByID() {


        Project project = new Project();
        project.setName("testProject");


        projectService.deleteProject(1L);

        List<Project> projects = projectService.getAllProjects();
        assertEquals(0, projects.size());


    }

    @Test
    @DisplayName("Should throw exception when updating non-existent project")
    public void shouldThrowWhenUpdatingNonExistentProject() {
        // Given
        Long nonExistentProjectId = 99L;
        Project updatedProjectData = new Project();
        updatedProjectData.setName("patryk");

        when(projectRepository.findById(nonExistentProjectId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(RuntimeException.class, () -> {
            projectService.updateProject(nonExistentProjectId, updatedProjectData);
        });

        verify(projectRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should create a project")
    void shouldCreateProject() {
        // przygotuj obiekt zwracany przez save()
        Project savedProject = new Project();
        savedProject.setId(1L);
        savedProject.setName("testProject");


        Project inputProject = new Project();
        inputProject.setName("testProject");


        when(projectRepository.save(inputProject)).thenReturn(savedProject);


        when(projectRepository.findAll()).thenReturn(List.of(savedProject));

        projectService.createProject(inputProject);
        List<Project> projects = projectService.getAllProjects();

        assertEquals(1, projects.size());
        assertEquals("testProject", projects.get(0).getName());
        verify(projectRepository, times(1)).save(inputProject);
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should update a project")
    void shouldUpdateProject() {
        // Given
        Long projectId = 1L;

        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setName("Old Project");

        Project inputProject = new Project();
        inputProject.setName("Updated Project");

        Project updatedProject = new Project();
        updatedProject.setId(projectId);
        updatedProject.setName("Updated Project");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(existingProject)).thenReturn(updatedProject);

        // When
        projectService.updateProject(projectId, inputProject);

        // Then
        assertEquals("Updated Project", existingProject.getName());
        verify(projectRepository).findById(projectId);
        verify(projectRepository).save(existingProject);
    }



}
