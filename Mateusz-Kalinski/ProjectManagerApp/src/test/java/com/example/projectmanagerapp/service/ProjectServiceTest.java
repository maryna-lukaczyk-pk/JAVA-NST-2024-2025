package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project1;
    private Project project2;

    @BeforeEach
    void setUp() {
        project1 = new Project();
        project1.setId(1L);
        project1.setName("TestProject1");

        project2 = new Project();
        project2.setId(2L);
        project2.setName("TestProject2");
    }

    @Test
    @DisplayName("Powinien zwrócić wszystkie projekty")
    void testGetAllProjects() {
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectService.getAllProjects();

        assertNotNull(projects);
        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Powinien zwrócić projekt po ID, gdy projekt istnieje")
    void testGetProjectById_whenProjectExists() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project1));

        Optional<Project> foundProjectOptional = projectService.getProjectById(1L);

        assertTrue(foundProjectOptional.isPresent());
        assertEquals(project1.getName(), foundProjectOptional.get().getName());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Powinien zwrócić pusty Optional, gdy projekt o danym ID nie istnieje")
    void testGetProjectById_whenProjectDoesNotExist() {
        when(projectRepository.findById(3L)).thenReturn(Optional.empty());

        Optional<Project> foundProjectOptional = projectService.getProjectById(3L);

        assertFalse(foundProjectOptional.isPresent());
        verify(projectRepository, times(1)).findById(3L);
    }

    @Test
    @DisplayName("Powinien utworzyć nowy projekt")
    void testCreateProject() {
        Project newProject = new Project();
        newProject.setName("NewProject");

        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> {
            Project p = invocation.getArgument(0);
            if (p.getId() == null) {
                p.setId(3L);
            }
            return p;
        });

        Project createdProject = projectService.createProject(newProject);

        assertNotNull(createdProject);
        assertEquals("NewProject", createdProject.getName());
        assertNotNull(createdProject.getId());
        verify(projectRepository, times(1)).save(newProject);
    }

    @Test
    @DisplayName("Powinien zaktualizować istniejący projekt")
    void testUpdateProject_whenProjectExists() {
        Project projectDetailsToUpdate = new Project();
        projectDetailsToUpdate.setName("UpdatedProject");

        when(projectRepository.existsById(1L)).thenReturn(true);
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> {
            Project p = invocation.getArgument(0);
            if (p.getId() == null) p.setId(1L);
            return p;
        });

        Project updatedProject = projectService.updateProject(1L, projectDetailsToUpdate);

        assertNotNull(updatedProject);
        assertEquals("UpdatedProject", updatedProject.getName());
        assertEquals(1L, updatedProject.getId());
        verify(projectRepository, times(1)).existsById(1L);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    @DisplayName("Powinien zwrócić null podczas aktualizacji, gdy projekt nie istnieje")
    void testUpdateProject_whenProjectDoesNotExist() {
        Project projectDetailsToUpdate = new Project();
        projectDetailsToUpdate.setName("UpdatedProject");
        when(projectRepository.existsById(3L)).thenReturn(false);

        Project result = projectService.updateProject(3L, projectDetailsToUpdate);

        assertNull(result);
        verify(projectRepository, times(1)).existsById(3L);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("Powinien usunąć projekt po ID, gdy projekt istnieje") // Zmieniłem trochę nazwę dla jasności
    void testDeleteProject_whenProjectExists() {
        // 1. Zaaranżuj (Arrange): Mockujemy zachowanie repozytorium
        when(projectRepository.existsById(1L)).thenReturn(true); // <-- DODAJ TEN MOCK
        doNothing().when(projectRepository).deleteById(1L);

        // 2. Działaj (Act): Wywołaj testowaną metodę
        // Użyj assertDoesNotThrow, aby upewnić się, że wyjątek nie jest rzucany,
        // gdy projekt istnieje
        assertDoesNotThrow(() -> projectService.deleteProject(1L));

        // 3. Asercje (Assert): Zweryfikuj, czy odpowiednie metody repozytorium zostały wywołane
        verify(projectRepository, times(1)).existsById(1L); // Dobrze jest też to zweryfikować
        verify(projectRepository, times(1)).deleteById(1L);
    }
}
