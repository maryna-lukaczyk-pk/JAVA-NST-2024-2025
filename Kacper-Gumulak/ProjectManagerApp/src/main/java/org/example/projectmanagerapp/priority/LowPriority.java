package org.example.projectmanagerapp.priority;

import org.example.projectmanagerapp.priority.PriorityLevel;

public class LowPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "LOW";
    }
}