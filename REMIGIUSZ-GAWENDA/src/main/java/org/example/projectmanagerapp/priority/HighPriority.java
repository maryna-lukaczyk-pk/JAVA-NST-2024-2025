package org.example.projectmanagerapp.priority;

public class HighPriority implements PriorityLevel {
    @Override
    public void getPriority() {
        System.out.println("High Priority");
    }
}
