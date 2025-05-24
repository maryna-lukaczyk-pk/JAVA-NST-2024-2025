// src/main/java/com/example/projectmanagerapp/entity/priority/LowPriority.java
package com.example.projectmanagerapp.entity.priority;

// Implementacja interfejsu PriorityLevel dla niskiego priorytetu.
public class LowPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "LOW"; // Zwraca "LOW" jako reprezentację niskiego priorytetu.
    }
}