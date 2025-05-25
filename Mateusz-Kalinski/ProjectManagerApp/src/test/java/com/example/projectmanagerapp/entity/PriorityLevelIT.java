package com.example.projectmanagerapp.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PriorityLevelIT {

    @Test
    void testHighPriority() {
        PriorityLevel priority = new HighPriority();
        assertEquals("HIGH", priority.getPriority(), "HighPriority should return 'HIGH'");
        // Możesz dodać więcej asercji, jeśli te klasy miałyby więcej logiki
    }

    @Test
    void testMediumPriority() {
        PriorityLevel priority = new MediumPriority();
        assertEquals("MEDIUM", priority.getPriority(), "MediumPriority should return 'MEDIUM'");
    }

    @Test
    void testLowPriority() {
        PriorityLevel priority = new LowPriority();
        assertEquals("LOW", priority.getPriority(), "LowPriority should return 'LOW'");
    }
}