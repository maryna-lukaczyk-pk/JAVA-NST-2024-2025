package org.example.projectmanagerapp.service.priority;

public class LowPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "LOW";
    }
}
