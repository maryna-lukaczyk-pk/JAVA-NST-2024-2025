package com.example.projectmanagerapp.service;

public class MediumPriority implements PriorityLevel {
    @Override
    public Priority getPriority() {
        return Priority.MEDIUM;
    }
}
