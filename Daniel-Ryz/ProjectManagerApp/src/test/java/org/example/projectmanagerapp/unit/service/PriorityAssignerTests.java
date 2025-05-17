package org.example.projectmanagerapp.unit.service;

import org.example.projectmanagerapp.service.priority.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PriorityAssignerTests {

    private PriorityAssigner priorityAssigner;

    @BeforeEach
    void setUp() {
        priorityAssigner = new PriorityAssigner();
    }

    @Test
    @DisplayName("Should assign High priority when task title contains '!'")
    void testAssignHighPriority() {
        // Arrange
        String taskTitle = "Critical task!";

        // Act
        PriorityLevel result = priorityAssigner.assignPriority(taskTitle);

        // Assert
        assertInstanceOf(HighPriority.class, result, "Priority should be High");
    }

    @Test
    @DisplayName("Should assign Medium priority when task title is longer than 20 characters")
    void testAssignMediumPriority() {
        // Arrange
        String taskTitle = "This is a long task title";

        // Act
        PriorityLevel result = priorityAssigner.assignPriority(taskTitle);

        // Assert
        assertInstanceOf(MediumPriority.class, result, "Priority should be Medium");
    }

    @Test
    @DisplayName("Should assign Low priority when task title is short and doesn't contain '!'")
    void testAssignLowPriority() {
        // Arrange
        String taskTitle = "Short task";

        // Act
        PriorityLevel result = priorityAssigner.assignPriority(taskTitle);

        // Assert
        assertInstanceOf(LowPriority.class, result, "Priority should be Low");
    }
}
