package org.example.projectmanagerapp.unit;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.schemas.ProjectDTO;
import org.example.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {
    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = Mockito.mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    @DisplayName("Should return all projects")
    void testGetAllProjects() {
        Project project1 = new Project();
        project1.setName("Project1");

        Project project2 = new Project();
        project2.setName("Project2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectService.findAllProjects();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return project by ID")
    void testFindProjectById() throws NotFoundException {
        Project project = new Project();
        project.setName("Projekt1");

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        Project result = projectService.findProjectById(1);

        assertEquals("Projekt1", result.getName());
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should add a new project")
    void testAddProject() {
        ProjectDTO dto = new ProjectDTO();
        dto.setName("NowyProject");

        projectService.addProject(dto);

        Mockito.verify(projectRepository, times(1))
                .save(argThat(project ->
                        project.getName().equals("NowyProject")
                ));
    }

    @Test
    @DisplayName("Should delete project when ID exists")
    void testDeleteProject_whenExists() throws NotFoundException {
        when(projectRepository.existsById(1)).thenReturn(true);

        projectService.deleteProject(1);

        verify(projectRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Should throw NotFoundException when deleting non-existing project")
    void testDeleteProject_whenNotExists() {
        when(projectRepository.existsById(1)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> projectService.deleteProject(1));
    }

    @Test
    @DisplayName("Should update existing project")
    void testUpdateProject_WhenExists() throws NotFoundException {
        when(projectRepository.existsById(1)).thenReturn(true);

        ProjectDTO dto = new ProjectDTO();
        dto.setName("UpdatedProject");

        projectService.updateProject(dto, 1);

        verify(projectRepository, times(1))
                .save(argThat(project ->
                        project.getId() == 1 && project.getName().equals("UpdatedProject")
                ));
    }

    @Test
    @DisplayName("Should throw NotFoundException when updating non-existing project")
    void testUpdateProject_whenNotExists() {
        when(projectRepository.existsById(1)).thenReturn(false);

        ProjectDTO dto = new ProjectDTO();
        dto.setName("ProjectNotExist");

        assertThrows(NotFoundException.class, () -> projectService.updateProject(dto, 1));
    }
}
