package org.example.projectmanagerapp.taskmanagement.priority;

import org.example.projectmanagerapp.taskmanagement.PriorityLevel;

public class MediumPriority implements PriorityLevel {
    @Override
    public Integer getPriority() {
        return 2;
    }
}
