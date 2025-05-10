package com.example.projectmanagerapp.entity;

import com.example.projectmanagerapp.priority.HighPriority;
import com.example.projectmanagerapp.priority.LowPriority;
import com.example.projectmanagerapp.priority.MediumPriority;
import com.example.projectmanagerapp.priority.PriorityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskTest {

    @Test
    @DisplayName("Task getters and setters should work correctly")
    void taskGettersAndSettersTest() {
        Task task = new Task();
        task.setTitle("testTask");
        task.setDescription("testDescription");
        task.setTaskTypeEnum(TaskType.BUG);
        
        assertEquals("testTask", task.getTitle());
        assertEquals("testDescription", task.getDescription());
        assertEquals(TaskType.BUG, task.getTaskTypeEnum());
        
        Project project = new Project();
        project.setName("testProject");
        
        task.setProject(project);
        assertNotNull(task.getProject());
        assertEquals("testProject", task.getProject().getName());
        
        PriorityLevel highPriority = new HighPriority();
        task.setPriority(highPriority);
        assertEquals("HIGH", task.getPriority().getPriority());
        
        PriorityLevel mediumPriority = new MediumPriority();
        task.setPriority(mediumPriority);
        assertEquals("MEDIUM", task.getPriority().getPriority());
        
        PriorityLevel lowPriority = new LowPriority();
        task.setPriority(lowPriority);
        assertEquals("LOW", task.getPriority().getPriority());
    }
    
    @Test
    @DisplayName("Task enum values should be correct")
    void taskEnumTest() {
        assertEquals(3, TaskType.values().length);
        assertEquals(TaskType.BUG, TaskType.valueOf("BUG"));
        assertEquals(TaskType.FEATURE, TaskType.valueOf("FEATURE"));
        assertEquals(TaskType.IMPROVEMENT, TaskType.valueOf("IMPROVEMENT"));
    }
}