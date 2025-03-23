package org.example.projectmanagerapp.priority;

public class LowPriority implements PriorityLevel {
    @Override
    public void getPriority() {
        System.out.println("Low Priority");
    }
}
