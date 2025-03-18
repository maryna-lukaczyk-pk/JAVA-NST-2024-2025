package org.example.projectmanagerapp.priority;

import org.example.projectmanagerapp.priority.PriorityLevel;

public class MediumPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "MEDIUM";
    }
}