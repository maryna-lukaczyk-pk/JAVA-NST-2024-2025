package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.task_type;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testy integracyjne dla logiki biznesowej klasy Task
 */
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
    public void testTaskPriorityLevels() {
        // Test dla wysokiego priorytetu
        Task highTask = new Task();
        highTask.setTask_type(task_type.high);
        highTask.setPriorityLevel();
        assertTrue(highTask.getPriorityLevel() instanceof HighPriority);
        assertEquals("high", highTask.getPriorityLevel().getPriority());

        // Test dla średniego priorytetu
        Task mediumTask = new Task();
        mediumTask.setTask_type(task_type.medium);
        mediumTask.setPriorityLevel();
        assertTrue(mediumTask.getPriorityLevel() instanceof MediumPriority);
        assertEquals("medium", mediumTask.getPriorityLevel().getPriority());

        // Test dla niskiego priorytetu
        Task lowTask = new Task();
        lowTask.setTask_type(task_type.low);
        lowTask.setPriorityLevel();
        assertTrue(lowTask.getPriorityLevel() instanceof LowPriority);
        assertEquals("low", lowTask.getPriorityLevel().getPriority());

        // Test dla null priorytetu (domyślny)
        Task nullTask = new Task();
        nullTask.setTask_type(null);
        nullTask.setPriorityLevel();
        assertTrue(nullTask.getPriorityLevel() instanceof MediumPriority);
        assertEquals("medium", nullTask.getPriorityLevel().getPriority());
    }

    @Test
    public void testTaskConstructor() {
        // Prosty test konstruktora
        Task task = new Task(0, "Test Task");
        assertEquals(0, task.getId());
        assertEquals("Test Task", task.getTitle());
    }


    @Test
    public void testUtworzenieNowegoZadania() throws Exception {
        // Utworzenie projektu jako zależności
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
        task.setTask_type(task_type.low);

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
    public void testAktualizacjaZadania() throws Exception {
        // Utworzenie projektu
        Project project = new Project();
        project.setName("ProjektZadania3");
        String projectJson = objectMapper.writeValueAsString(project);
        String projectResponse = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Project createdProject = objectMapper.readValue(projectResponse, Project.class);

        // Utworzenie zadania
        Task task = new Task();
        task.setTitle("ZadanieStare");
        task.setDescription("OpisStary");
        task.setTask_type(task_type.low);
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

        // Aktualizacja zadania
        Task updated = new Task();
        updated.setTitle("ZadanieNowe");
        updated.setDescription("OpisNowy");
        updated.setTask_type(task_type.medium);
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
        task.setTask_type(task_type.medium);
        

        assertEquals(42, task.getId());
        assertEquals("Testowy tytuł", task.getTitle());
        assertEquals("Testowy opis", task.getDescription());
        assertEquals(task_type.medium, task.getTask_type());
        

        task.setPriorityLevel();
        assertNotNull(task.getPriorityLevel());
        assertEquals("medium", task.getPriorityLevel().getPriority());
    }

    @Test
    public void testTaskEnumHandling() {

        Task task = new Task();
        

        task.setTask_type(task_type.low);
        assertEquals(task_type.low, task.getTask_type());
        
        task.setTask_type(task_type.medium);
        assertEquals(task_type.medium, task.getTask_type());
        
        task.setTask_type(task_type.high);
        assertEquals(task_type.high, task.getTask_type());
    }
}