package org.example.projectmanagerapp.priority;

import org.example.projectmanagerapp.priority.PriorityLevel;

// Klasa implementująca średni poziom priorytetu dla zadań
public class MediumPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "MEDIUM";
    }
}