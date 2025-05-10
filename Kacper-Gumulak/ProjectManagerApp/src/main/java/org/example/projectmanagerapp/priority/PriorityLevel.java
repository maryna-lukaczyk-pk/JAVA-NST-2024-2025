package org.example.projectmanagerapp.priority;

// Klasa implementująca interfejs dla poziomów priorytetu
public interface PriorityLevel {
    public String getPriority();
    PriorityLevel UNDEFINED = new UndefinedPriority();
    PriorityLevel LOW = new LowPriority();
    PriorityLevel MEDIUM = new MediumPriority();
    PriorityLevel HIGH = new HighPriority();
}