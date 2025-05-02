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
        Project project2 = new Project();

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectService.getAllProjects();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return chosen project")
    void testGetProjectById() {
        Project project = new Project();
        project.setId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project projectRetrieved = projectService.getProjectById(1L);

        assertEquals(project, projectRetrieved);
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception on missing project")
    void testGetProjectByIdException() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            projectService.getProjectById(1L);
        } catch (RuntimeException e) {
            assertEquals("Project does not exist", e.getMessage());
        }

        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new project")
    void testCreateProject() {
        Project project = new Project();

        when(projectRepository.save(project)).thenReturn(project);

        Project createdProject = projectService.createProject(project);

        assertEquals(project, createdProject);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Should update a chosen project")
    void testUpdateProject() {
        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("Project 1");

        Project project2 = new Project();
        project2.setId(1L);
        project2.setName("Project 1 updated");

        when(projectRepository.existsById(1L)).thenReturn(true);
        when(projectRepository.save(project2)).thenReturn(project2);

        Project project1Updated = projectService.updateById(1L, project2);

        assertEquals(project2, project1Updated);
        verify(projectRepository, times(1)).existsById(1L);
        verify(projectRepository, times(1)).save(project2);
    }

    @Test
    @DisplayName("Should delete a chosen project")
    void testDeleteProject() {
        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("Project 1");

        when(projectRepository.existsById(1L)).thenReturn(true);

        Boolean deleted = projectService.deleteById(1L);

        assertEquals(true, deleted);

        when(projectRepository.existsById(1L)).thenReturn(false);

        deleted = projectService.deleteById(1L);

        assertEquals(false, deleted);

        verify(projectRepository, times(2)).existsById(1L);
        verify(projectRepository, times(1)).deleteById(1L);
    }
}
