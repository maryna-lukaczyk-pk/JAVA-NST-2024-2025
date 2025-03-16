package com.example.projectmanagerapp.service;

public class HighPriority implements PriorityLevel {
    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }
}
