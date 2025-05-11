package org.example.projectmanagerapp.service.priority;

public class HighPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "HIGH";
    }
}
