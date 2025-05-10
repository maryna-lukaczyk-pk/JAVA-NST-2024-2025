package org.example.projectmanagerapp.priority;

public class HighPriority implements PriorityLevel {


    @Override
    public void getPriorityLevel() {
        System.out.println("High Priority");
    }
}
