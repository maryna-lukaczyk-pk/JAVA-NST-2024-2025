package org.example.projectmanagerapp.priority;

public class LowPriority implements PriorityLevel {
    @Override
    public void getPriorityLevel() {
        System.out.printf("Low Priority");
    }
}
