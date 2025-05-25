package com.example.projectmanagerapp.integration.controller;

import com.example.projectmanagerapp.integration.config.TestDatabaseConfig;
import com.example.projectmanagerapp.integration.utilities.AssertionHelper;
import org.example.projectmanager.dto.project.ProjectDto;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanager.dto.project.ProjectCreateDto;
import org.example.projectmanager.dto.project.ProjectEditDto;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String BASE_URL = "/api/projects";

    private String GetIdUrl(Long id) {
        return BASE_URL + "/" + id;
    }

    @Test
    public void testProjectGetAll() throws Exception {
        var dto1 = new ProjectCreateDto();
        dto1.name = "Project 1";
        Long id1 = CreateProject(dto1);

        var dto2 = new ProjectCreateDto();
        dto2.name = "Project 2";
        Long id2 = CreateProject(dto2);

        var createdProject1 = getProjectDto(id1);
        var createdProject2 = getProjectDto(id2);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ProjectDto[] projects = objectMapper.readValue(content, ProjectDto[].class);
                    boolean found1 = Arrays.asList(projects).contains(createdProject1);
                    Assertions.assertTrue(found1);

                    boolean found2 = Arrays.asList(projects).contains(createdProject2);
                    Assertions.assertTrue(found2);
                });
    }

    @Test
    public void testProjectCreation() throws Exception {
        ProjectCreateDto dto = new ProjectCreateDto();
        dto.name = "New Project";
        long id = CreateProject(dto);
        Assertions.assertTrue(id > 0);

        var project = getProjectDto(id);
        Assertions.assertEquals(id, project.id);
        Assertions.assertEquals(dto.name, project.name);
    }

    @Test
    public void testProjectGetNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(GetIdUrl(99999L)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testProjectGetAndDelete() throws Exception {
        ProjectCreateDto dto = new ProjectCreateDto();
        dto.name = "Integration Project";
        long id = CreateProject(dto);

        var project = getProjectDto(id);
        Assertions.assertEquals(id, project.id);

        mockMvc.perform(MockMvcRequestBuilders.delete(GetIdUrl(id)))
                .andExpect(status().isOk())
                .andExpect(AssertionHelper::AssertIsEmpty);

        mockMvc.perform(MockMvcRequestBuilders.get(GetIdUrl(id)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testProjectUpdate() throws Exception {
        var oldName = "ToUpdate";
        var newName = "UpdatedName";

        ProjectCreateDto createDto = new ProjectCreateDto();
        createDto.name = oldName;
        long id = CreateProject(createDto);

        ProjectEditDto editDto = new ProjectEditDto();
        editDto.id = id;
        editDto.name = newName;

        String updateJson = objectMapper.writeValueAsString(editDto);
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(AssertionHelper::AssertIsEmpty);

        var project = getProjectDto(id);
        Assertions.assertEquals(editDto.id, project.id);
        Assertions.assertEquals(editDto.name, project.name);
    }

    private @NotNull Long CreateProject(ProjectCreateDto dto) throws Exception {
        String json = objectMapper.writeValueAsString(dto);
        String response = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return Long.valueOf(response);
    }

    private ProjectDto getProjectDto(Long id) throws Exception {
        var response = mockMvc.perform(MockMvcRequestBuilders.get(GetIdUrl(id)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(response, ProjectDto.class);
    }
}
