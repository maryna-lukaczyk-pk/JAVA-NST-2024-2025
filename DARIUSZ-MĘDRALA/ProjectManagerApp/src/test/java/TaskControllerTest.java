import org.example.projectmanagerapp.controller.TaskController;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    void findAll_shouldReturnAllTasks() {

        Task task1 = new Task();
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setTitle("Task 2");

        List<Task> expectedTasks = Arrays.asList(task1, task2);

        when(taskService.findAll()).thenReturn(expectedTasks);

        List<Task> actualTasks = taskController.getAllTasks();

        assertEquals(expectedTasks.size(), actualTasks.size());
        assertEquals(expectedTasks, actualTasks);
        verify(taskService, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a new task and return created status")
    void createTask_shouldCreateNewTaskAndReturnCreatedStatus() {

        Task task = new Task();
        task.setTitle("Task 1");

        when(taskService.createTask(task)).thenReturn(task);

        ResponseEntity<Task> response = taskController.createTask(task);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(task, response.getBody());
        verify(taskService, times(1)).createTask(task);
    }

    @Test
    @DisplayName("Should return task by ID and return OK status")
    void getTaskById_shouldReturnTaskByIdAndReturnOkStatus() {

        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Task 1");

        when(taskService.findById(taskId)).thenReturn(task);

        ResponseEntity<Task> response = taskController.getTaskById(taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task, response.getBody());
        verify(taskService, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("Should delete a task and return no content status")
    void deleteTask_shouldDeleteTaskAndReturnNoContentStatus() {

        Long taskId = 1L;

        ResponseEntity<Void> response = taskController.deleteTask(taskId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskService, times(1)).deleteTask(taskId);
    }

    @Test
    @DisplayName("Should update a task and return OK status")
    void updateTask_shouldUpdateTaskAndReturnOkStatus() {

        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Updated Task");

        when(taskService.updateTask(taskId, task)).thenReturn(task);

        ResponseEntity<Task> response = taskController.updateTask(taskId, task);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task, response.getBody());
        verify(taskService, times(1)).updateTask(taskId, task);
    }
}
