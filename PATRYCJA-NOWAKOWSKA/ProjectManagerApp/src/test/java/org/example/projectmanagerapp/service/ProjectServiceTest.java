package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    @DisplayName("Powinno zwrócić wszystkie projekty")
    void shouldReturnAllProjects() {
        Project p1 = new Project();
        p1.setName("Project1");
        Project p2 = new Project();
        p2.setName("Project2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Project> projects = projectService.getAllProjects();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Powinno utworzyć nowy projekt")
    void shouldCreateProject() {
        Project project = new Project();
        project.setName("NewProject");
        project.setDescription("Description");

        when(projectRepository.save(project)).thenReturn(project);

        Project created = projectService.createProject(project);

        assertNotNull(created);
        assertEquals("NewProject", created.getName());
        assertEquals("Description", created.getDescription());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Powinno zwrócić projekt po ID")
    void shouldReturnProjectById() {
        Project project = new Project();
        project.setName("TestProject");
        project.setDescription("Desc");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<Project> found = projectService.getProjectById(1L);

        assertTrue(found.isPresent());
        assertEquals("TestProject", found.get().getName());
        assertEquals("Desc", found.get().getDescription());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Powinno zaktualizować projekt")
    void shouldUpdateProject() {
        Project existingProject = new Project();
        existingProject.setName("OldName");
        existingProject.setDescription("OldDesc");

        Project updatedProject = new Project();
        updatedProject.setName("NewName");
        updatedProject.setDescription("NewDesc");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(existingProject)).thenReturn(existingProject);

        Project result = projectService.updateProject(1L, updatedProject);

        assertEquals("NewName", result.getName());
        assertEquals("NewDesc", result.getDescription());
        verify(projectRepository).findById(1L);
        verify(projectRepository).save(existingProject);
    }

    @Test
    @DisplayName("Powinno rzucić wyjątek, gdy projekt do aktualizacji nie istnieje")
    void shouldThrowExceptionWhenProjectNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                projectService.updateProject(1L, new Project()));

        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    @DisplayName("Powinno usunąć projekt po ID")
    void shouldDeleteProject() {
        doNothing().when(projectRepository).deleteById(1L);

        projectService.deleteProject(1L);

        verify(projectRepository, times(1)).deleteById(1L);
    }
}
