package org.example.projectmanagerapp.priority;

public class MediumPriority implements PriorityLevel {
    @Override
    public void getPriority() {
        System.out.println("Medium Priority");
    }
}
