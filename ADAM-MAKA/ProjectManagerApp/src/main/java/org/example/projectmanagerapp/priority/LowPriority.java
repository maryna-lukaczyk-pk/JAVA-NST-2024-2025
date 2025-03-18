package org.example.projectmanagerapp.priority;

public class LowPriority implements PriorityLevel {
    @Override
    public int getPriority() {
        return 1;
    }
}