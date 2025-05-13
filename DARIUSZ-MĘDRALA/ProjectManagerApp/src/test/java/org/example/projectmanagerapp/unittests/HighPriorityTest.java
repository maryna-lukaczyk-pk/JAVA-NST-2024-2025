package org.example.projectmanagerapp.unittests;

import org.example.projectmanagerapp.entity.HighPriority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HighPriorityTest {

    HighPriority highPriority = new HighPriority();

    @Test
    @DisplayName("Should return HIGH PRIORITY")
    void getPriorityLevel_shouldReturnHighPriority() {

        String expected = "HIGH PRIORITY";
        String actual = highPriority.getPriorityLevel();

        assertEquals(expected, actual);
    }
}
