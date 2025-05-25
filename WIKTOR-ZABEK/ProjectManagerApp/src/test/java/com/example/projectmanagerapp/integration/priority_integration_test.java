package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.tasks;
import com.example.projectmanagerapp.priority.high_priority;
import com.example.projectmanagerapp.priority.low_priority;
import com.example.projectmanagerapp.priority.medium_priority;
import com.example.projectmanagerapp.priority.priority_level;
import com.example.projectmanagerapp.repository.tasks_repository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class priority_integration_test extends integration_test_base {

    @Autowired
    private tasks_repository tasksRepository;

    @Test
    @DisplayName("Test Priority Implementation - Integration Test")
    void test_priority_implementation() {
        priority_level highPriority = new high_priority();
        assertEquals("HIGH", highPriority.get_priority());

        priority_level mediumPriority = new medium_priority();
        assertEquals("MEDIUM", mediumPriority.get_priority());

        priority_level lowPriority = new low_priority();
        assertEquals("LOW", lowPriority.get_priority());


        tasks task = new tasks();
        task.setTitle("Priority Test Task");
        task.setDescription("Testing priority setting");


        try {
            java.lang.reflect.Method method = tasks.class.getDeclaredMethod("get_priority_level", priority_level.class);
            method.setAccessible(true);
            method.invoke(task, highPriority);

            assertEquals("HIGH", task.getPriority());


            method.invoke(task, mediumPriority);
            assertEquals("MEDIUM", task.getPriority());

            method.invoke(task, lowPriority);
            assertEquals("LOW", task.getPriority());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test priority", e);
        }
    }
}