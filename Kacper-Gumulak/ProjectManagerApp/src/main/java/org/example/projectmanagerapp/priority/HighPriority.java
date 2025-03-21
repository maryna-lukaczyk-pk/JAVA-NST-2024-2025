package org.example.projectmanagerapp.priority;

import org.example.projectmanagerapp.priority.PriorityLevel;

public class HighPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "HIGH";
    }
}