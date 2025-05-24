package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProjectCrudIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void shouldCreateProject() throws Exception {
        // Given
        Project project = new Project();
        project.setName("New Project");

        // When
        MvcResult result = mockMvc.perform(post("/api/v1/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        Project createdProject = objectMapper.readValue(
                result.getResponse().getContentAsString(), Project.class);

        assertThat(createdProject.getId()).isNotNull();
        assertThat(createdProject.getName()).isEqualTo("New Project");

        // Verify in database
        assertThat(projectRepository.findById(createdProject.getId())).isPresent();
    }

    @Test
    @Transactional
    void shouldRetrieveProjectById() throws Exception {
        // Given
        Project project = new Project();
        project.setName("Test Project");
        Project savedProject = projectRepository.save(project);

        // When
        MvcResult result = mockMvc.perform(get("/api/v1/project/{id}", savedProject.getId()))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        Project retrievedProject = objectMapper.readValue(
                result.getResponse().getContentAsString(), Project.class);

        assertThat(retrievedProject.getId()).isEqualTo(savedProject.getId());
        assertThat(retrievedProject.getName()).isEqualTo("Test Project");
    }

    @Test
    void shouldReturnNotFoundForNonExistentProject() throws Exception {
        // When/Then
        mockMvc.perform(get("/api/v1/project/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void shouldRetrieveAllProjects() throws Exception {
        // Given
        Project project1 = new Project();
        project1.setName("Project 1");
        projectRepository.save(project1);

        Project project2 = new Project();
        project2.setName("Project 2");
        projectRepository.save(project2);

        // When
        MvcResult result = mockMvc.perform(get("/api/v1/project"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        Project[] projects = objectMapper.readValue(
                result.getResponse().getContentAsString(), Project[].class);

        assertThat(projects).hasSize(2);
        assertThat(projects).extracting(Project::getName)
                .containsExactlyInAnyOrder("Project 1", "Project 2");
    }

    @Test
    @Transactional
    void shouldUpdateProject() throws Exception {
        // Given
        Project project = new Project();
        project.setName("Original Name");
        Project savedProject = projectRepository.save(project);

        Project updateData = new Project();
        updateData.setName("Updated Name");

        // When
        MvcResult result = mockMvc.perform(put("/api/v1/project/{id}", savedProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        Project updatedProject = objectMapper.readValue(
                result.getResponse().getContentAsString(), Project.class);

        assertThat(updatedProject.getId()).isEqualTo(savedProject.getId());
        assertThat(updatedProject.getName()).isEqualTo("Updated Name");

        // Verify in database
        Project dbProject = projectRepository.findById(savedProject.getId()).orElse(null);
        assertThat(dbProject).isNotNull();
        assertThat(dbProject.getName()).isEqualTo("Updated Name");
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentProject() throws Exception {
        // Given
        Project updateData = new Project();
        updateData.setName("Updated Name");

        // When/Then
        mockMvc.perform(put("/api/v1/project/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void shouldDeleteProject() throws Exception {
        // Given
        Project project = new Project();
        project.setName("Project to Delete");
        Project savedProject = projectRepository.save(project);

        // Verify project exists
        assertThat(projectRepository.findById(savedProject.getId())).isPresent();

        // When
        mockMvc.perform(delete("/api/v1/project/{id}", savedProject.getId()))
                .andExpect(status().isNoContent());

        // Then
        assertThat(projectRepository.findById(savedProject.getId())).isEmpty();
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentProject() throws Exception {
        // When/Then
        mockMvc.perform(delete("/api/v1/project/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void shouldReturnEmptyListWhenNoProjects() throws Exception {
        // When
        MvcResult result = mockMvc.perform(get("/api/v1/project"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        Project[] projects = objectMapper.readValue(
                result.getResponse().getContentAsString(), Project[].class);

        assertThat(projects).isEmpty();
    }

    @Test
    @Transactional
    void shouldMaintainProjectUsersRelationshipAfterUpdate() throws Exception {
        // Given - Create project with user
        User user = new User();
        user.setUsername("testuser");
        User savedUser = userRepository.save(user);

        Project project = new Project();
        project.setName("Project with User");
        project.setUsers(new java.util.ArrayList<>());
        project.getUsers().add(savedUser);
        Project savedProject = projectRepository.save(project);

        // Verify initial state
        assertThat(savedProject.getUsers()).hasSize(1);

        // When - Update project name
        Project updateData = new Project();
        updateData.setName("Updated Project Name");

        mockMvc.perform(put("/api/v1/project/{id}", savedProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk());

        // Then - Verify users relationship is maintained
        Project updatedProject = projectRepository.findById(savedProject.getId()).orElse(null);
        assertThat(updatedProject).isNotNull();
        assertThat(updatedProject.getName()).isEqualTo("Updated Project Name");
        assertThat(updatedProject.getUsers()).hasSize(1);
        assertThat(updatedProject.getUsers().get(0).getId()).isEqualTo(savedUser.getId());
    }
}