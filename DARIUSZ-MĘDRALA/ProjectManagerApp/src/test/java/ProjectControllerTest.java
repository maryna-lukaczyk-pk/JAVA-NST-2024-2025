import org.example.projectmanagerapp.controller.ProjectController;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.services.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    void findAll_shouldReturnAllProjects() {

        Project project1 = new Project();
        project1.setName("Project 1");

        Project project2 = new Project();
        project2.setName("Project 2");

        List<Project> expectedProjects = Arrays.asList(project1, project2);

        when(projectService.findAll()).thenReturn(expectedProjects);

        List<Project> actualProjects = projectController.getProjects();

        assertEquals(expectedProjects.size(), actualProjects.size());
        assertEquals(expectedProjects, actualProjects);
        verify(projectService, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a new project and return created status")
    void createProject_shouldCreateNewProjectAndReturnCreatedStatus() {

        Project project = new Project();
        project.setName("Project 1");

        when(projectService.createProject(project)).thenReturn(project);

        ResponseEntity<Project> response = projectController.createProject(project);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(project, response.getBody());
        verify(projectService, times(1)).createProject(project);
    }

    @Test
    @DisplayName("Should return a project by ID and return OK status")
    void getProjectById_shouldReturnProjectAndReturnOkStatus() {

        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setName("Project 1");

        when(projectService.findById(projectId)).thenReturn(project);

        ResponseEntity<Project> response = projectController.getProjectById(projectId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(project, response.getBody());
        verify(projectService, times(1)).findById(projectId);
    }

    @Test
    @DisplayName("Should delete a project and return no content status")
    void deleteProject_shouldDeleteProjectAndReturnNoContentStatus() {

        Long projectId = 1L;

        ResponseEntity<Void> response = projectController.deleteProject(projectId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(projectService, times(1)).deleteProject(projectId);
    }

    @Test
    @DisplayName("Should update a project and return OK status")
    void updateProject_shouldUpdateProjectAndReturnOkStatus() {

        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setName("Updated Project");

        when(projectService.updateProject(projectId, project)).thenReturn(project);

        ResponseEntity<Project> response = projectController.updateProject(projectId, project);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(project, response.getBody());
        verify(projectService, times(1)).updateProject(projectId, project);
    }
}
