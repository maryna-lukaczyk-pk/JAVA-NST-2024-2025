package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.config.TestConfig;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.priority.HighPriority;
import com.example.projectmanagerapp.priority.MediumPriority;
import com.example.projectmanagerapp.priority.LowPriority;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
public class PriorityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testHighPriorityTask() throws Exception {
        Project project = new Project();
        project.setName("testHighPriorityProject");

        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult projectResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                projectResult.getResponse().getContentAsString(), Project.class);

        Tasks task = new Tasks();
        task.setTitle("testHighPriorityTask");
        task.setDescription("Test high priority task");
        task.setTask_type(Tasks.TaskType.NEW);
        task.setPriorityLevel(new HighPriority());
        task.setProject(createdProject);

        String taskJson = objectMapper.writeValueAsString(task);
        mockMvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.priority", is("HIGH")));
    }

    @Test
    public void testMediumPriorityTask() throws Exception {
        Project project = new Project();
        project.setName("testMediumPriorityProject");

        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult projectResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                projectResult.getResponse().getContentAsString(), Project.class);

        Tasks task = new Tasks();
        task.setTitle("testMediumPriorityTask");
        task.setDescription("Test medium priority task");
        task.setTask_type(Tasks.TaskType.NEW);
        task.setPriorityLevel(new MediumPriority());
        task.setProject(createdProject);

        String taskJson = objectMapper.writeValueAsString(task);
        mockMvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.priority", is("Medium")));
    }


    @Test
    public void testLowPriorityTask() throws Exception {
        Project project = new Project();
        project.setName("testLowPriorityProject");

        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult projectResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                projectResult.getResponse().getContentAsString(), Project.class);

        Tasks task = new Tasks();
        task.setTitle("testLowPriorityTask");
        task.setDescription("Test low priority task");
        task.setTask_type(Tasks.TaskType.NEW);
        task.setPriorityLevel(new LowPriority());
        task.setProject(createdProject);

        String taskJson = objectMapper.writeValueAsString(task);
        mockMvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.priority", is("Low")));
    }
}