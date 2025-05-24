// src/main/java/com/example/projectmanagerapp/entity/priority/HighPriority.java
package com.example.projectmanagerapp.entity.priority;

// import com.example.projectmanagerapp.PriorityLevel; // Niepotrzebny import, interfejs jest w tym samym pakiecie
// lub jeśli interfejs jest w innym pakiecie, to import jest potrzebny.
// Zakładając, że PriorityLevel jest teraz w com.example.projectmanagerapp.entity.priority:
// import com.example.projectmanagerapp.entity.priority.PriorityLevel; // To jest redundantne

// Implementacja interfejsu PriorityLevel dla wysokiego priorytetu.
public class HighPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "HIGH"; // Zwraca "HIGH" jako reprezentację wysokiego priorytetu.
    }
}