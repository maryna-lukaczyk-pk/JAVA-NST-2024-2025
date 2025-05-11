package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProjectControllerTest {

    @Mock private ProjectService projectService;
    @InjectMocks private ProjectController controller;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllProjects() throws Exception {
        Project p1 = new Project(); p1.setId(1L); p1.setName("A");
        Project p2 = new Project(); p2.setId(2L); p2.setName("B");
        when(projectService.findAll()).thenReturn(Arrays.asList(p1, p2));

        mvc.perform(get("/projects/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].name").value("B"));
    }

    @Test
    void getProjectById_found() throws Exception {
        Project p = new Project(); p.setId(1L); p.setName("A");
        when(projectService.findById(1L)).thenReturn(Optional.of(p));

        mvc.perform(get("/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("A"));
    }

    @Test
    void getProjectById_notFound() throws Exception {
        when(projectService.findById(99L)).thenReturn(Optional.empty());

        mvc.perform(get("/projects/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProject() throws Exception {
        Project p = new Project(); p.setId(1L); p.setName("X");
        when(projectService.create(any(Project.class))).thenReturn(p);

        mvc.perform(post("/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"X\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateProject_found() throws Exception {
        Project existing = new Project(); existing.setId(1L);
        when(projectService.findById(1L)).thenReturn(Optional.of(existing));
        Project updated = new Project(); updated.setId(1L); updated.setName("Y");
        when(projectService.update(eq(1L), any(Project.class))).thenReturn(updated);

        mvc.perform(put("/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Y\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Y"));
    }

    @Test
    void deleteProject() throws Exception {
        mvc.perform(delete("/projects/1"))
                .andExpect(status().isNoContent());
        verify(projectService).delete(1L);
    }
}
