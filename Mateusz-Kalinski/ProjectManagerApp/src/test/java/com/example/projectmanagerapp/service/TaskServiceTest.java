package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.entity.Project; // Potrzebne, jeśli zadania mają referencje do projektów
import com.example.projectmanagerapp.repository.TaskRepository;
// import com.example.projectmanagerapp.repository.ProjectRepository; // Odkomentuj, jeśli serwis zadań go używa
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    // @Mock // Odkomentuj, jeśli TaskService wstrzykuje ProjectRepository
    // private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;

    private Tasks task1;
    private Tasks task2;
    private Project associatedProject;

    @BeforeEach
    void setUp() {
        associatedProject = new Project();
        associatedProject.setId(10L);
        associatedProject.setName("Associated Project");

        task1 = new Tasks();
        task1.setId(1L);
        task1.setTitle("TestTask1");
        task1.setProject(associatedProject);

        task2 = new Tasks();
        task2.setId(2L);
        task2.setTitle("TestTask2");
    }

    @Test
    @DisplayName("Powinien zwrócić wszystkie zadania")
    void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Tasks> tasks = taskService.getAllTasks();

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Powinien zwrócić zadanie po ID, gdy zadanie istnieje")
    void testGetTaskById_whenTaskExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        Optional<Tasks> foundTaskOptional = taskService.getTaskById(1L);

        assertTrue(foundTaskOptional.isPresent());
        assertEquals(task1.getTitle(), foundTaskOptional.get().getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Powinien zwrócić pusty Optional, gdy zadanie o danym ID nie istnieje")
    void testGetTaskById_whenTaskDoesNotExist() {
        when(taskRepository.findById(3L)).thenReturn(Optional.empty());

        Optional<Tasks> foundTaskOptional = taskService.getTaskById(3L);

        assertFalse(foundTaskOptional.isPresent());
        verify(taskRepository, times(1)).findById(3L);
    }

    @Test
    @DisplayName("Powinien utworzyć nowe zadanie")
    void testCreateTask() {
        Tasks newTask = new Tasks();
        newTask.setTitle("NewTask");
        newTask.setProject(associatedProject); // Zakładamy, że projekt jest ustawiany przed wywołaniem serwisu

        when(taskRepository.save(any(Tasks.class))).thenAnswer(invocation -> {
            Tasks t = invocation.getArgument(0);
            if (t.getId() == null) {
                t.setId(3L);
            }
            return t;
        });
        // Jeśli TaskService miałby logikę pobierania Project przez ProjectRepository:
        // when(projectRepository.findById(anyLong())).thenReturn(Optional.of(associatedProject));

        Tasks createdTask = taskService.createTask(newTask); // Jeśli TaskService.createTask nie przyjmuje projectId

        assertNotNull(createdTask);
        assertEquals("NewTask", createdTask.getTitle());
        assertNotNull(createdTask.getId());
        if (createdTask.getProject() != null) {
            assertEquals(associatedProject.getId(), createdTask.getProject().getId());
        }
        verify(taskRepository, times(1)).save(newTask);
    }

    @Test
    @DisplayName("Powinien zaktualizować istniejące zadanie")
    void testUpdateTask_whenTaskExists() {
        Tasks taskDetailsToUpdate = new Tasks();
        taskDetailsToUpdate.setTitle("UpdatedTask");

        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.save(any(Tasks.class))).thenAnswer(invocation -> {
            Tasks t = invocation.getArgument(0);
            if (t.getId() == null) t.setId(1L);
            t.setProject(task1.getProject()); // Zachowaj oryginalny projekt, jeśli nie jest aktualizowany
            return t;
        });


        Tasks updatedTask = taskService.updateTask(1L, taskDetailsToUpdate);

        assertNotNull(updatedTask);
        assertEquals("UpdatedTask", updatedTask.getTitle());
        assertEquals(1L, updatedTask.getId());
        if (updatedTask.getProject() != null) {
            assertEquals(associatedProject.getId(), updatedTask.getProject().getId());
        }
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).save(any(Tasks.class));
    }

    @Test
    @DisplayName("Powinien zwrócić null podczas aktualizacji, gdy zadanie nie istnieje")
    void testUpdateTask_whenTaskDoesNotExist() {
        Tasks taskDetailsToUpdate = new Tasks();
        taskDetailsToUpdate.setTitle("UpdatedTask");
        when(taskRepository.existsById(3L)).thenReturn(false);

        Tasks result = taskService.updateTask(3L, taskDetailsToUpdate);

        assertNull(result);
        verify(taskRepository, times(1)).existsById(3L);
        verify(taskRepository, never()).save(any(Tasks.class));
    }

    @Test
    @DisplayName("Powinien usunąć zadanie po ID")
    void testDeleteTask_whenTaskExists() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }
}
