package com.example.projectmanagerapp.service;

public class LowPriority implements PriorityLevel {
    @Override
    public Priority getPriority() {
        return Priority.LOW;
    }
}
