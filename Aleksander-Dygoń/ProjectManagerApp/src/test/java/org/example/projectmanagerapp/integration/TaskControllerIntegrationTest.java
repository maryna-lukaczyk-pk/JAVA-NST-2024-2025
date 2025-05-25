package org.example.projectmanagerapp.integration;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class TaskControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testCreateAndGetTask() throws Exception {
        Project project = new Project();
        project.setName("Test Project");
        Project savedProject = projectRepository.save(project);

        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setTaskType(Task.TaskType.HIGH_PRIORITY);
        task.setProject(savedProject);

        String taskJson = objectMapper.writeValueAsString(task);

        String responseContent = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Task createdTask = objectMapper.readValue(responseContent, Task.class);

        mockMvc.perform(get("/tasks/" + createdTask.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test Task"));
    }

    @Test
    public void testUpdateTask() throws Exception {
        Project project = projectRepository.save(new Project());

        Task task = new Task();
        task.setTitle("Original Title");
        task.setProject(project);
        Task savedTask = taskRepository.save(task);

        Task update = new Task();
        update.setTitle("Updated Title");

        mockMvc.perform(put("/tasks/" + savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks/" + savedTask.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Title"));
    }

    @Test
    public void testDeleteTask() throws Exception {
        Project project = projectRepository.save(new Project());

        Task task = new Task();
        task.setTitle("To Be Deleted");
        task.setProject(project);
        Task savedTask = taskRepository.save(task);

        mockMvc.perform(delete("/tasks/" + savedTask.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks/" + savedTask.getId()))
                .andExpect(status().isNotFound());

        assertFalse(taskRepository.findById(savedTask.getId()).isPresent());
    }

    @Test
    public void testGetNonExistentTask() throws Exception {
        mockMvc.perform(get("/tasks/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateNonExistentTask() throws Exception {
        Task update = new Task();
        update.setTitle("Updated Title");

        mockMvc.perform(put("/tasks/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteNonExistentTask() throws Exception {
        mockMvc.perform(delete("/tasks/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateTaskWithAllTaskTypes() throws Exception {
        Project project = projectRepository.save(new Project());

        Task highTask = new Task();
        highTask.setTitle("High Priority Task");
        highTask.setTaskType(Task.TaskType.HIGH_PRIORITY);
        highTask.setProject(project);

        String highTaskJson = objectMapper.writeValueAsString(highTask);
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(highTaskJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskType").value("HIGH_PRIORITY"));

        Task mediumTask = new Task();
        mediumTask.setTitle("Medium Priority Task");
        mediumTask.setTaskType(Task.TaskType.MEDIUM_PRIORITY);
        mediumTask.setProject(project);

        String mediumTaskJson = objectMapper.writeValueAsString(mediumTask);
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mediumTaskJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskType").value("MEDIUM_PRIORITY"));

        Task lowTask = new Task();
        lowTask.setTitle("Low Priority Task");
        lowTask.setTaskType(Task.TaskType.LOW_PRIORITY);
        lowTask.setProject(project);

        String lowTaskJson = objectMapper.writeValueAsString(lowTask);
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lowTaskJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskType").value("LOW_PRIORITY"));
    }
}