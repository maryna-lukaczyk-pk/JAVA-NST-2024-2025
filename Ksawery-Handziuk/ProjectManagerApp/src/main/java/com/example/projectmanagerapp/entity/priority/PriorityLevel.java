// src/main/java/com/example/projectmanagerapp/entity/priority/PriorityLevel.java
package com.example.projectmanagerapp.entity.priority; // Zmieniono pakiet

// Interfejs definiujący kontrakt dla poziomów priorytetu.
// Każda klasa implementująca ten interfejs musi dostarczyć metodę getPriority().
public interface PriorityLevel {
    String getPriority(); // Metoda zwracająca nazwę priorytetu jako String (np. "HIGH", "MEDIUM", "LOW")
}