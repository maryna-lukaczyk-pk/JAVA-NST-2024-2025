package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Projects;
import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {
    private ProjectRepository projectRepository;
    private UserRepository    userRepository;
    private ProjectService    projectService;
    private Projects          project;
    private User              user;

    @BeforeEach
    void setUp() {
        projectRepository = Mockito.mock(ProjectRepository.class);
        userRepository    = Mockito.mock(UserRepository.class);
        projectService    = new ProjectService(projectRepository, userRepository);

        project = new Projects();
        project.setId(1L);
        project.setName("proj");
        project.setTasks(new java.util.HashSet<>());
        project.setUsers(new java.util.HashSet<>());

        user = new User();
        user.setId(2L);
        user.setUsername("testUser");
        user.setProjects(new java.util.HashSet<>());
    }

    @Test
    @DisplayName("Should return all projects")
    void testGetAllProjects() {
        Projects p1 = new Projects(); p1.setName("P1");
        Projects p2 = new Projects(); p2.setName("P2");
        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Projects> result = projectService.getAllProjects();
        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Nested
    @DisplayName("getProjectById")
    class GetByIdTests {
        @Test
        @DisplayName("returns project when found")
        void returnsWhenFound() {
            when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

            Projects result = projectService.getProjectById(1L);
            assertEquals("proj", result.getName());
            verify(projectRepository).findById(1L);
        }

        @Test
        @DisplayName("throws EntityNotFoundException when not found")
        void throwsWhenNotFound() {
            when(projectRepository.findById(2L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class,
                    () -> projectService.getProjectById(2L));
        }
    }

    @Test
    @DisplayName("createProject should save and return project")
    void testCreateProject() {
        when(projectRepository.save(project)).thenReturn(project);

        Projects result = projectService.createProject(project);
        assertSame(project, result);
        verify(projectRepository).save(project);
    }

    @Nested
    @DisplayName("updateProject")
    class UpdateTests {
        @Test
        @DisplayName("updates existing project")
        void updatesWhenExists() {
            Projects update = new Projects();
            update.setName("newName");
            update.setTasks(Collections.emptySet());
            update.setUsers(Collections.emptySet());

            when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
            when(projectRepository.save(any(Projects.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            Projects result = projectService.updateProject(1L, update);
            assertEquals("newName", result.getName());
            verify(projectRepository).findById(1L);
            verify(projectRepository).save(result);
        }

        @Test
        @DisplayName("throws when updating non-existing project")
        void throwsWhenNotExists() {
            when(projectRepository.findById(5L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class,
                    () -> projectService.updateProject(5L, project));
        }
    }

    @Nested
    @DisplayName("deleteProject")
    class DeleteTests {
        @Test
        @DisplayName("deletes existing project")
        void deletesWhenExists() {
            when(projectRepository.existsById(1L)).thenReturn(true);

            assertDoesNotThrow(() -> projectService.deleteProject(1L));
            verify(projectRepository).existsById(1L);
            verify(projectRepository).deleteById(1L);
        }

        @Test
        @DisplayName("throws when deleting non-existing project")
        void throwsWhenNotExists() {
            when(projectRepository.existsById(3L)).thenReturn(false);

            assertThrows(EntityNotFoundException.class,
                    () -> projectService.deleteProject(3L));
        }
    }

    @Nested
    @DisplayName("assignUserToProject")
    class AssignTests {
        @Test
        @DisplayName("assigns user when both exist")
        void assignsWhenExists() {
            when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
            when(userRepository.findById(2L)).thenReturn(Optional.of(user));

            assertDoesNotThrow(() -> projectService.assignUserToProject(1L, 2L));
            assertTrue(project.getUsers().contains(user));
            assertTrue(user.getProjects().contains(project));
            verify(userRepository).save(user);
        }

        @Test
        @DisplayName("throws when project not found")
        void throwsWhenProjectNotFound() {
            when(projectRepository.findById(9L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class,
                    () -> projectService.assignUserToProject(9L, 2L));
        }

        @Test
        @DisplayName("throws when user not found")
        void throwsWhenUserNotFound() {
            when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
            when(userRepository.findById(3L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class,
                    () -> projectService.assignUserToProject(1L, 3L));
        }
    }
}
