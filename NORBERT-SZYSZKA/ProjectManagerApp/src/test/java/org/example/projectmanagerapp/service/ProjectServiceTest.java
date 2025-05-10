package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock private ProjectRepository projectRepository;
    @InjectMocks private ProjectService projectService;
    @Captor private ArgumentCaptor<Project> captor;

    private Project p1, p2;

    @BeforeEach
    void setUp() {
        p1 = new Project(); p1.setId(1L); p1.setName("Alpha");
        p2 = new Project(); p2.setId(2L); p2.setName("Beta");
    }

    @Test
    void findAll_returnsAll() {
        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Project> result = projectService.findAll();
        assertThat(result).containsExactly(p1, p2);
        verify(projectRepository).findAll();
    }

    @Test
    void findById_existing_returnsProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(p1));

        Optional<Project> result = projectService.findById(1L);
        assertThat(result).contains(p1);
        verify(projectRepository).findById(1L);
    }

    @Test
    void findById_nonExisting_returnsEmpty() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Project> result = projectService.findById(99L);
        assertThat(result).isEmpty();
        verify(projectRepository).findById(99L);
    }

    @Test
    void create_savesAndReturns() {
        Project toSave = new Project(); toSave.setName("Gamma");
        when(projectRepository.save(any(Project.class))).thenReturn(p1);

        Project result = projectService.create(toSave);
        assertThat(result).isEqualTo(p1);
        verify(projectRepository).save(toSave);
    }

    @Test
    void update_setsIdAndSaves() {
        Project upd = new Project(); upd.setName("Updated");
        when(projectRepository.save(any(Project.class))).thenReturn(upd);

        Project result = projectService.update(7L, upd);
        assertThat(result).isEqualTo(upd);
        verify(projectRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(7L);
    }

    @Test
    void delete_invokesRepository() {
        doNothing().when(projectRepository).deleteById(1L);
        projectService.delete(1L);
        verify(projectRepository).deleteById(1L);
    }
}
