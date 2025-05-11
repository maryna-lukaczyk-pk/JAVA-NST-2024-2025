package org.example.projectmanagerapp;

import org.example.projectmanagerapp.priority.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriorityLevelTest {

    @Test
    @DisplayName("HighPriority should return 'High'")
    void testHighPriority() {
        PriorityLevel high = new HighPriority();
        String result = high.getPriority();
        assertEquals("High", result);
    }

    @Test
    @DisplayName("MediumPriority should return 'Medium'")
    void testMediumPriority() {
        PriorityLevel medium = new MediumPriority();
        String result = medium.getPriority();
        assertEquals("Medium", result);
    }

    @Test
    @DisplayName("LowPriority should return 'Low'")
    void testLowPriority() {
        PriorityLevel low = new LowPriority();
        String result = low.getPriority();
        assertEquals("Low", result);
    }
}
