package com.example.projectmanagerapp.entity;

import com.example.projectmanagerapp.priority.HighPriority;
import com.example.projectmanagerapp.priority.LowPriority;
import com.example.projectmanagerapp.priority.MediumPriority;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskTest {

    @Test
    void setPriorityLevel_whenTaskTypeIsNull_setsMediumPriority() {

        Task task = new Task();
        task.setTaskType(null);

        task.setPriorityLevel();

        assertTrue(task.getPriorityLevel() instanceof MediumPriority);
        assertEquals("medium", task.getPriorityLevel().getPriority());
    }

    @Test
    void setPriorityLevel_whenTaskTypeIsHigh_setsHighPriority() {

        Task task = new Task();
        task.setTaskType(TaskType.high);

        task.setPriorityLevel();

        assertTrue(task.getPriorityLevel() instanceof HighPriority);
        assertEquals("high", task.getPriorityLevel().getPriority());
    }

    @Test
    void setPriorityLevel_whenTaskTypeIsMedium_setsMediumPriority() {

        Task task = new Task();
        task.setTaskType(TaskType.medium);

        task.setPriorityLevel();

        assertTrue(task.getPriorityLevel() instanceof MediumPriority);
        assertEquals("medium", task.getPriorityLevel().getPriority());
    }

    @Test
    void setPriorityLevel_whenTaskTypeIsLow_setsLowPriority() {
        // given
        Task task = new Task();
        task.setTaskType(TaskType.low);


        task.setPriorityLevel();


        assertTrue(task.getPriorityLevel() instanceof LowPriority);
        assertEquals("low", task.getPriorityLevel().getPriority());
    }


    @Test
    void setPriorityLevel_whenTaskTypeIsUnknown_setsMediumPriority() {

        Task task = new Task();

        task.setPriorityLevel();

        assertTrue(task.getPriorityLevel() instanceof MediumPriority);
        assertEquals("medium", task.getPriorityLevel().getPriority());
    }
}