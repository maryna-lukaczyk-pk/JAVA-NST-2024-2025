package org.example.projectmanager.taskmanagement.priority;

import org.example.projectmanager.taskmanagement.PriorityLevel;

public class MediumPriority implements PriorityLevel {
    @Override
    public Integer getPriority() {
        return 2;
    }
}
