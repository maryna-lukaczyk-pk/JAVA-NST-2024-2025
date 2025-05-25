package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.ITTestConfiguration;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {ITTestConfiguration.class})
@AutoConfigureMockMvc
public class ProjectIntegrationTestIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void shouldDeleteProjectById() throws Exception {
        // given
        Project project = new Project();
        project.setName("Test Project");
        projectRepository.save(project);

        // when
        mockMvc.perform(delete("/api/tasks/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // then
        List<Project> all = projectRepository.findAll();
        assertThat(all.isEmpty()).isTrue();
    }

    @Test
    void shouldFindProjectById() throws Exception {
        // given
        Project project = new Project();
        project.setName("Test Project");
        Project savedProject = projectRepository.save(project);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/tasks/" + savedProject.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

        // then
        Project resultProject = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Project.class);
        assertThat(resultProject).isNotNull();
        assertThat(resultProject.getName()).isEqualTo(project.getName());

        Project projectFromDb = projectRepository.findById(resultProject.getId()).orElseThrow();
        assertThat(projectFromDb.getId()).isEqualTo(savedProject.getId());
    }

    @Test
    void shouldCreateProject() throws Exception {
        // given
        Project project = new Project();
        project.setName("New Project");

        // when
        MvcResult mvcResult = mockMvc.perform(post("/api/tasks")
                        .content(objectMapper.writeValueAsString(project))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

        // then
        Project resultProject = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Project.class);
        assertThat(resultProject).isNotNull();
        assertThat(resultProject.getName()).isEqualTo(project.getName());

        Project projectFromDb = projectRepository.findById(resultProject.getId()).orElseThrow();
        assertThat(projectFromDb.getName()).isEqualTo(project.getName());
    }

    @Test
    void shouldFindAllProjects() throws Exception {
        // given
        Project project1 = new Project();
        Project project2 = new Project();
        project1.setName("Project 1");
        project2.setName("Project 2");

        projectRepository.save(project1);
        projectRepository.save(project2);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

        // then
        String response = mvcResult.getResponse().getContentAsString();
        List<Project> returnedProjects = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Project.class));

        assertThat(returnedProjects.size()).isEqualTo(2);
        assertThat(returnedProjects.get(0).getName()).isEqualTo("Project 1");
        assertThat(returnedProjects.get(1).getName()).isEqualTo("Project 2");
    }

    @Test
    void shouldUpdateProject() throws Exception {
        // given
        Project existingProject = new Project();
        existingProject.setName("Old Name");
        Project savedProject = projectRepository.save(existingProject);

        Project updatedProject = new Project();
        updatedProject.setName("New Name");

        // when
        mockMvc.perform(put("/api/tasks/" + savedProject.getId())
                        .content(objectMapper.writeValueAsString(updatedProject))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk());

        // then
        Optional<Project> result = projectRepository.findById(savedProject.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("New Name");
    }
}