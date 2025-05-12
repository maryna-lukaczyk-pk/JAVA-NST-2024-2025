package com.example.projectmanagerapp.priority;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriorityLevelTest {

    @Test
    void highPriorityReturnsHigh() {
        PriorityLevel p = new HighPriority();
        assertEquals("High", p.getPriority());
    }

    @Test
    void mediumPriorityReturnsMedium() {
        PriorityLevel p = new MediumPriority();
        assertEquals("Medium", p.getPriority());
    }

    @Test
    void lowPriorityReturnsLow() {
        PriorityLevel p = new LowPriority();
        assertEquals("Low", p.getPriority());
    }
}