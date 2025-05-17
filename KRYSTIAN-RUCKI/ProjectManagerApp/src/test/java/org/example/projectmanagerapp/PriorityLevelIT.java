package org.example.projectmanagerapp;

import org.example.projectmanagerapp.priority.HighPriority;
import org.example.projectmanagerapp.priority.LowPriority;
import org.example.projectmanagerapp.priority.MediumPriority;
import org.example.projectmanagerapp.priority.PriorityLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriorityLevelIT {

    @Test
    void testAllPriorityLevels() {
        PriorityLevel high = new HighPriority();
        PriorityLevel medium = new MediumPriority();
        PriorityLevel low = new LowPriority();

        assertEquals("High", high.getPriority());
        assertEquals("Medium", medium.getPriority());
        assertEquals("Low", low.getPriority());
    }
}
