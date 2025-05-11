package org.example.projectmanagerapp.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class TaskTypeTest {
    @Test
    void enumContainsAllValues() {
        assertThat(TaskType.values())
                .containsExactlyInAnyOrder(TaskType.LOW_PRIORITY, TaskType.MEDIUM_PRIORITY, TaskType.HIGH_PRIORITY);
    }
}
