package s.kopec.ProjectManagerApp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import s.kopec.ProjectManagerApp.entity.Project;
import s.kopec.ProjectManagerApp.repository.ProjectRepository;
import s.kopec.ProjectManagerApp.service.ProjectService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
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
        project1.setName("Project Alpha");

        Project project2 = new Project();
        project2.setName("Project Beta");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> result = projectService.getAllProjects();

        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return project by ID if exists")
    void testFindProjectById() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.findProjectById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Project", result.get().getName());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new project")
    void testCreateProject() {
        Project newProject = new Project();
        newProject.setName("New Project");

        when(projectRepository.save(newProject)).thenReturn(newProject);

        Project result = projectService.createProject(newProject);

        assertNotNull(result);
        assertEquals("New Project", result.getName());
        verify(projectRepository, times(1)).save(newProject);
    }

    @Test
    @DisplayName("Should delete a project by ID")
    void testDeleteProjectById() {
        projectService.deleteProjectById(1L);
        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should update project name when project exists")
    void testUpdateProjectName_ProjectExists() {
        Long projectId = 1L;
        String newName = "Updated Project Name";

        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setName("Old Name");

        when(projectRepository.existsById(projectId)).thenReturn(true);
        when(projectRepository.getReferenceById(projectId)).thenReturn(existingProject);
        when(projectRepository.save(existingProject)).thenReturn(existingProject);

        projectService.updateProjectName(projectId, newName);

        assertEquals(newName, existingProject.getName());
        verify(projectRepository).existsById(projectId);
        verify(projectRepository).getReferenceById(projectId);
        verify(projectRepository).save(existingProject);
    }

    @Test
    @DisplayName("Should not update project name if project does not exist")
    void testUpdateProjectName_ProjectDoesNotExist() {
        Long projectId = 2L;

        when(projectRepository.existsById(projectId)).thenReturn(false);

        projectService.updateProjectName(projectId, "Won't be set");

        verify(projectRepository).existsById(projectId);
        verify(projectRepository, never()).getReferenceById(any());
        verify(projectRepository, never()).save(any());
    }
}
