package org.example.projectmanagerapp.unittests;

import org.example.projectmanagerapp.entity.MediumPriority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MediumPriorityTest {

    MediumPriority mediumPriority = new MediumPriority();

    @Test
    @DisplayName("Should return MEDIUM PRIORITY")
    void getPriorityLevel_shouldReturnHighPriority() {

        String expected = "MEDIUM PRIORITY";
        String actual = mediumPriority.getPriorityLevel();

        assertEquals(expected, actual);
    }
}
