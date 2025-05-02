import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TaskServiceTest {
    private TaskService taskService;
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    void testGetAllTasks() {
        Task task1 = new Task();
        Task task2 = new Task();

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return chosen task")
    void testGetTaskById() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task taskRetrieved = taskService.getTaskById(1L);

        assertEquals(task, taskRetrieved);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception on missing task")
    void testGetTaskByIdException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            taskService.getTaskById(1L);
        } catch (RuntimeException e) {
            assertEquals("Task does not exist", e.getMessage());
        }

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new task")
    void testCreateTask() {
        Task task = new Task();
        task.setTitle("Task 1");

        when(taskRepository.save(task)).thenReturn(task);

        Task createdTask = taskService.createTask(task);
        assertEquals(task, createdTask);

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should update a chosen task")
    void testUpdateTask() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(1L);
        task2.setTitle("Task 1 - Updated");

        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.save(task2)).thenReturn(task2);

        Task task1Updated = taskService.updateById(1L, task2);

        assertEquals(task2, task1Updated);
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).save(task2);
    }

    @Test
    @DisplayName("Should delete a chosen task")
    void testDeleteTask() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        when(taskRepository.existsById(1L)).thenReturn(true);

        Boolean deleted = taskService.deleteById(1L);

        assertEquals(true, deleted);

        when(taskRepository.existsById(1L)).thenReturn(false);

        deleted = taskService.deleteById(1L);

        assertEquals(false, deleted);

        verify(taskRepository, times(2)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }
}
