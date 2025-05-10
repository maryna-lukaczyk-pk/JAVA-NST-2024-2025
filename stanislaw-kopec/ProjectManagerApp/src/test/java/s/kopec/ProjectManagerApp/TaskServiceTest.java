package s.kopec.ProjectManagerApp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import s.kopec.ProjectManagerApp.entity.Task;
import s.kopec.ProjectManagerApp.repository.TaskRepository;
import s.kopec.ProjectManagerApp.service.TaskService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    void testGetAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setTitle("Task 2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return task by ID if exists")
    void testGetTaskById() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Task", result.get().getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new task")
    void testCreateTask() {
        Task newTask = new Task();
        newTask.setTitle("New Task");

        when(taskRepository.save(newTask)).thenReturn(newTask);

        Task createdTask = taskService.createTask(newTask);

        assertNotNull(createdTask);
        assertEquals("New Task", createdTask.getTitle());
        verify(taskRepository, times(1)).save(newTask);
    }

    @Test
    @DisplayName("Should delete task by ID")
    void testDeleteTaskById() {
        taskService.deleteTaskById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should update task title when task exists")
    void testUpdateTaskTitle_TaskExists() {
        Long taskId = 1L;
        String newTitle = "Updated Task";

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Title");

        when(taskRepository.existsById(taskId)).thenReturn(true);
        when(taskRepository.getReferenceById(taskId)).thenReturn(existingTask);
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        taskService.updateTaskTitle(taskId, newTitle);

        assertEquals(newTitle, existingTask.getTitle());
        verify(taskRepository).existsById(taskId);
        verify(taskRepository).getReferenceById(taskId);
        verify(taskRepository).save(existingTask);
    }

    @Test
    @DisplayName("Should not update task title when task does not exist")
    void testUpdateTaskTitle_TaskDoesNotExist() {
        Long taskId = 2L;
        when(taskRepository.existsById(taskId)).thenReturn(false);

        taskService.updateTaskTitle(taskId, "Title That Won't Be Set");

        verify(taskRepository).existsById(taskId);
        verify(taskRepository, never()).getReferenceById(any());
        verify(taskRepository, never()).save(any());
    }
}
