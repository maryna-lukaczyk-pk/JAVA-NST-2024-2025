package org.example.projectmanagerapp.priority;

import org.example.projectmanagerapp.priority.PriorityLevel;

// Klasa implementująca nie określony poziom priorytetu dla zadań
public class UndefinedPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "UNDEFINED";
    }
}