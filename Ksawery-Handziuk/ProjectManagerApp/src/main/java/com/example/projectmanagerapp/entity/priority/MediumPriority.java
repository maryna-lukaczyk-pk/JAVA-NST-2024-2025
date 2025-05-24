// src/main/java/com/example/projectmanagerapp/entity/priority/MediumPriority.java
package com.example.projectmanagerapp.entity.priority;

// Implementacja interfejsu PriorityLevel dla średniego priorytetu.
public class MediumPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "MEDIUM"; // Zwraca "MEDIUM" jako reprezentację średniego priorytetu.
    }
}