package org.example.projectmanagerapp.service;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.entity.Tasks.TaskType;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    public void testGetAllTasks() {
        Tasks task1 = new Tasks();
        task1.setTitle("Task1");

        Tasks task2 = new Tasks();
        task2.setTitle("Task2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Tasks> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return task by ID")
    public void testGetByID() {
        Tasks task = new Tasks();
        task.setId((1L));
        task.setTitle("TestTask");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Tasks> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals("TestTask", result.get().getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new task")
    public void testCreateTask() {
        Tasks newTask = new Tasks();
        newTask.setTitle("NewTask");

        when(taskRepository.save(newTask)).thenReturn(newTask);

        Tasks createdTask = taskService.createTask(newTask);

        assertNotNull(createdTask);
        assertEquals("NewTask", createdTask.getTitle());
        verify(taskRepository, times(1)).save(newTask);
    }

    @Test
    @DisplayName("Should update an existing task")
    public void testUpdateTask() {
        Tasks existingTask = new Tasks();
        existingTask.setId(1L);
        existingTask.setTitle("ExistingTask");

        Tasks updatedTask = new Tasks();
        updatedTask.setTitle("UpdatedTask");
        updatedTask.setDescription("UpdatedDescription");
        updatedTask.setTaskType(TaskType.HIGH_PRIORITY);
        updatedTask.setProject(null);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Optional<Tasks> result = taskService.updateTask(1L, updatedTask);

        assertTrue(result.isPresent());
        assertEquals("UpdatedTask", result.get().getTitle());
        assertEquals("UpdatedDescription", result.get().getDescription());
        assertEquals(TaskType.HIGH_PRIORITY, result.get().getTaskType());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    @DisplayName("Should delete an existing task")
    public void testDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        boolean result = taskService.deleteTask(1L);

        assertTrue(result);
        verify(taskRepository, times(1)).deleteById(1L);
    }
}
