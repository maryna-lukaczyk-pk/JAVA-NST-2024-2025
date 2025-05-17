package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.TaskType;
import com.example.projectmanagerapp.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TaskServiceIT extends BaseIT {

    @Autowired
    private TaskService taskService;

    @Test
    public void testGetAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setTaskType(TaskType.high);
        taskService.create(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setTaskType(TaskType.medium);
        taskService.create(task2);

        List<Task> tasks = taskService.getAll();

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertTrue(tasks.stream().anyMatch(t -> "Task 1".equals(t.getTitle())));
        assertTrue(tasks.stream().anyMatch(t -> "Task 2".equals(t.getTitle())));
    }

    @Test
    public void testGetTaskById() {
        // Utworzenie zadania
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setTaskType(TaskType.low);
        Task savedTask = taskService.create(task);

        Task retrievedTask = taskService.getById(savedTask.getId());

        assertNotNull(retrievedTask);
        assertEquals(savedTask.getId(), retrievedTask.getId());
        assertEquals("Test Task", retrievedTask.getTitle());
        assertEquals("Test Description", retrievedTask.getDescription());
        assertEquals(TaskType.low, retrievedTask.getTaskType());
    }

    @Test
    public void testGetTaskByIdNotFound() {
        assertThrows(NoSuchElementException.class, () -> {
            taskService.getById(9999);
        });
    }

    @Test
    public void testDeleteTask() {
        Task task = new Task();
        task.setTitle("Task to Delete");
        task.setDescription("This task will be deleted");
        task.setTaskType(TaskType.medium);
        Task savedTask = taskService.create(task);

        assertNotNull(taskService.getById(savedTask.getId()));

        taskService.delete(savedTask.getId());

        assertThrows(NoSuchElementException.class, () -> {
            taskService.getById(savedTask.getId());
        });
    }

    @Test
    public void testDeleteTaskNotFound() {
        assertThrows(NoSuchElementException.class, () -> {
            taskService.delete(9999);
        });
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task();
        task.setTitle("Original Title");
        task.setDescription("Original Description");
        task.setTaskType(TaskType.low);
        Task savedTask = taskService.create(task);

        Task updates = new Task();
        updates.setTitle("Updated Title");
        updates.setDescription("Updated Description");
        updates.setTaskType(TaskType.high);

        Task updatedTask = taskService.update(savedTask.getId(), updates);

        assertNotNull(updatedTask);
        assertEquals(savedTask.getId(), updatedTask.getId());
        assertEquals("Updated Title", updatedTask.getTitle());
        assertEquals("Updated Description", updatedTask.getDescription());
        assertEquals(TaskType.high, updatedTask.getTaskType());
    }

    @Test
    public void testUpdateTaskNotFound() {
        Task updates = new Task();
        updates.setTitle("Updated Title");

        assertThrows(NoSuchElementException.class, () -> {
            taskService.update(9999, updates);
        });
    }
}