package org.example.projectmanager.taskmanagement.priority;

import org.example.projectmanager.taskmanagement.PriorityLevel;

public class HighPriority implements PriorityLevel {
    @Override
    public Integer getPriority() {
        return 3;
    }
}
