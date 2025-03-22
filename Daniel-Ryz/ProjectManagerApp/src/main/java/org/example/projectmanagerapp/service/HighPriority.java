package org.example.projectmanagerapp.service;

public class HighPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "HIGH";
    }
}
