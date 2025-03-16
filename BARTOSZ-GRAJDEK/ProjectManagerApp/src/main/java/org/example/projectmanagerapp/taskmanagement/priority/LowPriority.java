package org.example.projectmanagerapp.taskmanagement.priority;

import org.example.projectmanagerapp.taskmanagement.PriorityLevel;

public class LowPriority implements PriorityLevel {
    @Override
    public Integer getPriority() {
        return 1;
    }
}
