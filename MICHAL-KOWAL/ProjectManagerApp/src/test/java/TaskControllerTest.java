import org.example.projectmanagerapp.controller.TaskController;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TaskControllerTest {
    private TaskController taskController;
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        taskService = Mockito.mock(TaskService.class);
        taskController = new TaskController(taskService);
    }

    @Test
    @DisplayName("Should return all tasks")
    void testGetAllTasks() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");

        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskController.all();

        assertEquals(2, tasks.size());
        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    @DisplayName("Should create a new task")
    void testCreateTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");

        when(taskService.createTask(task)).thenReturn(task);

        Task createdTask = taskController.newTask(task);
        assertEquals(task, createdTask);

        verify(taskService, times(1)).createTask(task);
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

        when(taskService.updateTaskById(1L, task2)).thenReturn(task2);

        var task1UpdateResult = taskController.updateTask(1L, task2);

        assertEquals(task2, task1UpdateResult.getBody());
        assertEquals(HttpStatus.OK, task1UpdateResult.getStatusCode());
        verify(taskService, times(1)).updateTaskById(1L, task2);
    }

    @Test
    @DisplayName("Should delete a chosen task")
    void testDeleteTask() {
        when(taskService.deleteTaskById(1L)).thenReturn(true);

        var deleteResult = taskController.deleteTask(1L);

        assertEquals(HttpStatus.OK, deleteResult.getStatusCode());

        when(taskService.deleteTaskById(1L)).thenReturn(false);

        deleteResult = taskController.deleteTask(1L);

        assertEquals(HttpStatus.BAD_REQUEST, deleteResult.getStatusCode());

        verify(taskService, times(2)).deleteTaskById(1L);
    }

    @Test
    @DisplayName("Should return chosen task")
    void testGetTaskById() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");

        when(taskService.getTaskById(1L)).thenReturn(task);

        var taskRetrieveResult = taskController.getTaskById(1L);

        assertEquals(task, taskRetrieveResult.getBody());
        assertEquals(HttpStatus.OK, taskRetrieveResult.getStatusCode());

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    @DisplayName("Should throw exception on missing task")
    void testGetTaskByIdException() {
        when(taskService.getTaskById(1L)).thenThrow(new RuntimeException("Task does not exist"));

        var taskRetrieveResult = taskController.getTaskById(1L);

        assertEquals(HttpStatus.BAD_REQUEST, taskRetrieveResult.getStatusCode());

        verify(taskService, times(1)).getTaskById(1L);
    }
}
