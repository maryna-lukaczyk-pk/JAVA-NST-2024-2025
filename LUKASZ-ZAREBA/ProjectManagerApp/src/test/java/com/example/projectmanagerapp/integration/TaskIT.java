package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.TaskType;
import com.example.projectmanagerapp.priority.HighPriority;
import com.example.projectmanagerapp.priority.LowPriority;
import com.example.projectmanagerapp.priority.MediumPriority;
import com.example.projectmanagerapp.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TaskIT extends BaseIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void testPriorityLevelForAllTaskTypes() {
        for (TaskType type : TaskType.values()) {
            Task task = new Task();
            task.setTaskType(type);
            task.setPriorityLevel();

            assertNotNull(task.getPriorityLevel());

            switch (type) {
                case high:
                    assertTrue(task.getPriorityLevel() instanceof HighPriority);
                    assertEquals("high", task.getPriorityLevel().getPriority());
                    break;
                case medium:
                    assertTrue(task.getPriorityLevel() instanceof MediumPriority);
                    assertEquals("medium", task.getPriorityLevel().getPriority());
                    break;
                case low:
                    assertTrue(task.getPriorityLevel() instanceof LowPriority);
                    assertEquals("low", task.getPriorityLevel().getPriority());
                    break;
                default:
                    fail("Nieoczekiwany typ zadania: " + type);
            }
        }

        Task nullTask = new Task();
        nullTask.setTaskType(null);
        nullTask.setPriorityLevel();
        assertTrue(nullTask.getPriorityLevel() instanceof MediumPriority);
        assertEquals("medium", nullTask.getPriorityLevel().getPriority());
    }

    @Test
    public void testTaskConstructor() {
        Task task = new Task(0, "Test Task");
        assertEquals(0, task.getId());
        assertEquals("Test Task", task.getTitle());
    }


    @Test
    public void testCreateNewTask() throws Exception {
        Project project = new Project();
        project.setName("Projekt dla zadania");
        String projectJson = objectMapper.writeValueAsString(project);
        String projectResponse = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Project createdProject = objectMapper.readValue(projectResponse, Project.class);


        Task task = new Task();
        task.setTitle("Nowe zadanie");
        task.setDescription("Opis zadania");
        task.setTaskType(TaskType.low);

        task.setPriorityLevel();
        task.setPriorityLevel(null);
        
        Project refProject = new Project();
        refProject.setId(createdProject.getId());
        task.setProject(refProject);
        String taskJson = objectMapper.writeValueAsString(task);


        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateTask() throws Exception {
        Project project = new Project();
        project.setName("ProjektZadania3");
        String projectJson = objectMapper.writeValueAsString(project);
        String projectResponse = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Project createdProject = objectMapper.readValue(projectResponse, Project.class);

        Task task = new Task();
        task.setTitle("ZadanieStare");
        task.setDescription("OpisStary");
        task.setTaskType(TaskType.low);
        Project ref = new Project();
        ref.setId(createdProject.getId());
        task.setProject(ref);
        String taskJson = objectMapper.writeValueAsString(task);
        String response = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Task createdTask = objectMapper.readValue(response, Task.class);

        Task updated = new Task();
        updated.setTitle("ZadanieNowe");
        updated.setDescription("OpisNowy");
        updated.setTaskType(TaskType.medium);
        Project refUpdated = new Project();
        refUpdated.setId(createdProject.getId());
        updated.setProject(refUpdated);
        String updateJson = objectMapper.writeValueAsString(updated);
        mockMvc.perform(put("/tasks/{id}", createdTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testTaskGettersSetters() {
        // Test getterów i setterów zadania
        Task task = new Task();
        task.setId(42);
        task.setTitle("Testowy tytuł");
        task.setDescription("Testowy opis");
        task.setTaskType(TaskType.medium);
        
        assertEquals(42, task.getId());
        assertEquals("Testowy tytuł", task.getTitle());
        assertEquals("Testowy opis", task.getDescription());
        assertEquals(TaskType.medium, task.getTaskType());
        

        task.setPriorityLevel();
        assertNotNull(task.getPriorityLevel());
        assertEquals("medium", task.getPriorityLevel().getPriority());
    }

    @Test
    public void testTaskEnumHandling() {

        Task task = new Task();
        

        task.setTaskType(TaskType.low);
        assertEquals(TaskType.low, task.getTaskType());
        
        task.setTaskType(TaskType.medium);
        assertEquals(TaskType.medium, task.getTaskType());
        
        task.setTaskType(TaskType.high);
        assertEquals(TaskType.high, task.getTaskType());
    }

    @Test
    public void testGetAllTasksEndpoint() throws Exception {
        Task task1 = new Task();
        task1.setTitle("Task 1 for GET All");
        task1.setDescription("Description 1");
        task1.setTaskType(TaskType.high);
        task1 = taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2 for GET All");
        task2.setDescription("Description 2");
        task2.setTaskType(TaskType.medium);
        task2 = taskRepository.save(task2);

        mockMvc.perform(get("/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[*].title", hasItems("Task 1 for GET All", "Task 2 for GET All")));
    }

    @Test
    public void testGetTaskByIdEndpoint() throws Exception {
        Task task = new Task();
        task.setTitle("Task for GET by ID");
        task.setDescription("Description for GET test");
        task.setTaskType(TaskType.low);
        task = taskRepository.save(task);

        mockMvc.perform(get("/tasks/{id}", task.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value("Task for GET by ID"))
                .andExpect(jsonPath("$.description").value("Description for GET test"));
    }

    @Test
    public void testDeleteTaskEndpoint() throws Exception {
        Task task = new Task();
        task.setTitle("Task to be deleted");
        task.setDescription("This task will be deleted via REST");
        task.setTaskType(TaskType.medium);
        task = taskRepository.save(task);

        assertTrue(taskRepository.existsById(task.getId()));

        mockMvc.perform(delete("/tasks/{id}", task.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertFalse(taskRepository.existsById(task.getId()));
    }

    @Test
    public void testDeleteTaskEndpointNotFound() throws Exception {
        int nonExistingId = 9999;
        assertFalse(taskRepository.existsById(nonExistingId));

        mockMvc.perform(delete("/tasks/{id}", nonExistingId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}