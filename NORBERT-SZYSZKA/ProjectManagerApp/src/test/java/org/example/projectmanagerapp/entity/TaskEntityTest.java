package org.example.projectmanagerapp.entity;

import org.example.projectmanagerapp.priority.MediumPriority;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class TaskEntityTest {
    @Test
    void idAndTitle() {
        Task t = new Task();
        t.setId(7L);
        t.setTitle("Z");
        assertThat(t.getId()).isEqualTo(7L);
        assertThat(t.getTitle()).isEqualTo("Z");
    }

    @Test
    void priorityLevel_defaultAndCustom() {
        Task t = new Task();
        assertThat(t.getPriorityLevel()).isEqualTo("Not set");
        t.setPriorityLevel(new MediumPriority());
        assertThat(t.getPriorityLevel()).isEqualTo("Medium Priority");
    }
}
