package org.example.projectmanagerapp.integration;

import org.example.projectmanagerapp.utils.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
public class PriorityUtilsIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("Test PriorityFactory creates correct priority objects")
    public void testPriorityFactory() {
        PriorityLevel highPriority = PriorityFactory.createPriority("high");
        assertTrue(highPriority instanceof HighPriority);
        assertEquals("High", highPriority.getPriority());

        PriorityLevel mediumPriority = PriorityFactory.createPriority("medium");
        assertTrue(mediumPriority instanceof MediumPriority);
        assertEquals("Medium", mediumPriority.getPriority());

        PriorityLevel lowPriority = PriorityFactory.createPriority("low");
        assertTrue(lowPriority instanceof LowPriority);
        assertEquals("Low", lowPriority.getPriority());

        PriorityLevel defaultPriority = PriorityFactory.createPriority("invalid");
        assertTrue(defaultPriority instanceof MediumPriority);
        assertEquals("Medium", defaultPriority.getPriority());
    }
}