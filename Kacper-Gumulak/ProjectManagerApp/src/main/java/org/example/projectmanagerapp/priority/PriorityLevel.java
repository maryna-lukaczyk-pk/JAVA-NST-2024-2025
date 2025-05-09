package org.example.projectmanagerapp.priority;

// Klasa implementująca interfejs dla poziomów priorytetu
public interface PriorityLevel {
    public String getPriority();
    PriorityLevel UNDEFINED = new UndefinedPriority();
}