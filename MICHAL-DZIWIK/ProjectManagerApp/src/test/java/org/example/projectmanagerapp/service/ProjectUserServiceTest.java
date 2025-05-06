package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.ProjectUser;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectUserServiceTest {

    @Mock
    private ProjectUserRepository projectUserRepository;

    private ProjectUserService projectUserService;

    @BeforeEach
    void setUp() {
        projectUserService = new ProjectUserService(projectUserRepository);
    }

    @Test
    @DisplayName("getAllProjectUsers should return list of all project-users")
    void getAllProjectUsers_returnsAll() {
        ProjectUser pu1 = new ProjectUser();
        pu1.setId(1L);
        Project p1 = new Project(); p1.setId(1L); pu1.setProject(p1);
        User u1 = new User(); u1.setId(1L); pu1.setUser(u1);
        ProjectUser pu2 = new ProjectUser();
        pu2.setId(2L); Project p2 = new Project(); p2.setId(2L); pu2.setProject(p2);
        User u2 = new User(); u2.setId(2L); pu2.setUser(u2);
        when(projectUserRepository.findAll()).thenReturn(Arrays.asList(pu1, pu2));

        List<ProjectUser> result = projectUserService.getAllProjectUsers();

        assertThat(result).hasSize(2);
        verify(projectUserRepository).findAll();
    }

    @Test
    @DisplayName("createProjectUser should save and return project-user")
    void createProjectUser_savesAndReturns() {
        ProjectUser pu = new ProjectUser(); Project p = new Project(); p.setId(1L); pu.setProject(p);
        User u = new User(); u.setId(1L); pu.setUser(u);
        ProjectUser saved = new ProjectUser(); saved.setId(1L); saved.setProject(p); saved.setUser(u);
        when(projectUserRepository.save(pu)).thenReturn(saved);

        ProjectUser result = projectUserService.createProjectUser(pu);

        assertThat(result).isEqualTo(saved);
        verify(projectUserRepository).save(pu);
    }

    @Test
    @DisplayName("getProjectUserById when found returns project-user")
    void getById_found_returnsPU() {
        ProjectUser pu = new ProjectUser(); pu.setId(1L);
        when(projectUserRepository.findById(1L)).thenReturn(Optional.of(pu));

        ProjectUser result = projectUserService.getProjectUserById(1L);

        assertThat(result).isEqualTo(pu);
        verify(projectUserRepository).findById(1L);
    }

    @Test
    @DisplayName("updateProjectUser when exists updates and returns")
    void updateProjectUser_exists_updatesAndReturns() {
        ProjectUser existing = new ProjectUser(); existing.setId(1L);
        Project newP = new Project(); newP.setId(2L); existing.setProject(newP);
        User newU = new User(); newU.setId(2L); existing.setUser(newU);
        ProjectUser details = new ProjectUser(); Project dp = new Project(); dp.setId(2L); details.setProject(dp);
        User du = new User(); du.setId(2L); details.setUser(du);
        when(projectUserRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(projectUserRepository.save(existing)).thenReturn(existing);

        ProjectUser result = projectUserService.updateProjectUser(1L, details);

        assertThat(result.getProject().getId()).isEqualTo(2L);
        assertThat(result.getUser().getId()).isEqualTo(2L);
        verify(projectUserRepository).findById(1L);
        verify(projectUserRepository).save(existing);
    }

    @Test
    @DisplayName("deleteProjectUser when exists deletes")
    void deleteProjectUser_exists_deletes() {
        when(projectUserRepository.existsById(1L)).thenReturn(true);

        projectUserService.deleteProjectUser(1L);

        verify(projectUserRepository).existsById(1L);
        verify(projectUserRepository).deleteById(1L);
    }
}
