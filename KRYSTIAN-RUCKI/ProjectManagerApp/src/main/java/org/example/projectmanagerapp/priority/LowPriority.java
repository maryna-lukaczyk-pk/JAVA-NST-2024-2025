package org.example.projectmanagerapp.priority;

public class LowPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "Low";
    }
}