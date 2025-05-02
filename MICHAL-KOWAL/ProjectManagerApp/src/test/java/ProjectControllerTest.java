import org.example.projectmanagerapp.controller.ProjectController;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProjectControllerTest {
    private ProjectController projectController;
    private ProjectService projectService;

    @BeforeEach
    public void setUp() {
        projectService = Mockito.mock(ProjectService.class);
        projectController = new ProjectController(projectService);
    }

    @Test
    @DisplayName("Should return all projects")
    void testGetAllProjects() {
        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("Project 1");

        Project project2 = new Project();
        project2.setId(2L);
        project2.setName("Project 2");

        when(projectService.getAllProjects()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectController.all();

        assertEquals(2, projects.size());
        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    @DisplayName("Should create a new project")
    void testCreateProject() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Project 1");

        when(projectService.createProject(project)).thenReturn(project);

        Project createdProject = projectController.newProject(project);

        assertEquals(project, createdProject);
        verify(projectService, times(1)).createProject(project);
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

        when(projectService.updateProjectById(1L, project2)).thenReturn(project2);

        var project1UpdateResult = projectController.updateProject(1L, project2);

        assertEquals(project2, project1UpdateResult.getBody());
        assertEquals(HttpStatus.OK, project1UpdateResult.getStatusCode());
        verify(projectService, times(1)).updateProjectById(1L, project2);
    }

    @Test
    @DisplayName("Should delete a chosen project")
    void testDeleteProject() {
        when(projectService.deleteProjectById(1L)).thenReturn(true);

        var deleteResult = projectController.deleteProject(1L);

        assertEquals(HttpStatus.OK, deleteResult.getStatusCode());

        when(projectService.deleteProjectById(1L)).thenReturn(false);

        deleteResult = projectController.deleteProject(1L);

        assertEquals(HttpStatus.BAD_REQUEST, deleteResult.getStatusCode());

        verify(projectService, times(2)).deleteProjectById(1L);
    }

    @Test
    @DisplayName("Should return chosen project")
    void testGetProjectById() {
        Project project = new Project();
        project.setId(1L);

        when(projectService.getProjectById(1L)).thenReturn(project);

        var projectRetrieveResult = projectController.getProjectById(1L);

        assertEquals(project, projectRetrieveResult.getBody());
        assertEquals(HttpStatus.OK, projectRetrieveResult.getStatusCode());

        verify(projectService, times(1)).getProjectById(1L);
    }

    @Test
    @DisplayName("Should throw exception on missing project")
    void testGetProjectByIdException() {
        when(projectService.getProjectById(1L)).thenThrow(new RuntimeException("Project does not exist"));

        var projectRetrieveResult = projectController.getProjectById(1L);

        assertEquals(HttpStatus.BAD_REQUEST, projectRetrieveResult.getStatusCode());

        verify(projectService, times(1)).getProjectById(1L);
    }
}
