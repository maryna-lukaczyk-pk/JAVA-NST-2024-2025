package com.example.projectmanagerapp.entity;

import com.example.projectmanagerapp.priority.HighPriority;
import com.example.projectmanagerapp.priority.MediumPriority;
import com.example.projectmanagerapp.priority.LowPriority;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskEntityTest {

    @Test
    void defaultPriorityWhenNoneSet() {
        Task t = new Task();
        // nic nie ustawiamy → fallback powinien być Medium
        assertEquals("Medium", t.getPriority().getPriority());
    }

    @Test
    void settingPriorityLevelUpdatesName() {
        Task t = new Task();
        t.setPriority(new HighPriority());
        assertEquals("High", t.getPriorityName());
        assertTrue(t.getPriority() instanceof HighPriority);
    }

    @Test
    void priorityNameMappingForExistingName() {
        Task t = new Task();
        // dzięki lombokowi mamy setter
        t.setPriorityName("Low");
        assertTrue(t.getPriority() instanceof LowPriority);
        assertEquals("Low", t.getPriority().getPriority());
    }

    @Test
    void unknownPriorityNameFallsBack() {
        Task t = new Task();
        t.setPriorityName("UNKNOWN");
        // nieznany string → domyślnie Medium
        assertTrue(t.getPriority() instanceof MediumPriority);
    }
}