package org.example.projectmanagerapp.priority;

import org.example.projectmanagerapp.priority.PriorityLevel;

// Klasa implementująca wysoki poziom priorytetu dla zadań
public class HighPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "HIGH";
    }
}