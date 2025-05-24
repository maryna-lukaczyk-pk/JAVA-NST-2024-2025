// src/main/java/com/example/projectmanagerapp/entity/Task.java
package com.example.projectmanagerapp.entity;

import com.example.projectmanagerapp.entity.priority.PriorityLevel; // Poprawiony import
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter; // Lombok do automatycznego generowania getterów
import lombok.NoArgsConstructor; // Lombok do konstruktora bezargumentowego
import lombok.Setter; // Lombok do automatycznego generowania setterów

@Entity
@Table(name = "task") // Definicja tabeli dla zadań
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Reprezentuje zadanie w systemie.")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unikalny identyfikator zadania.", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Opis zadania.", example = "Zaimplementować logowanie", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    // Pole 'priority' jest oznaczone jako @Transient, co oznacza, że nie będzie
    // mapowane na kolumnę w bazie danych przez JPA.
    // Jego wartość będzie istniała tylko w pamięci obiektu Task.
    // Jeśli priorytet ma być trwały, należy usunąć @Transient i zaimplementować
    // mechanizm mapowania (np. przez @Enumerated, @Convert, lub dodatkowe pole).
    @Transient
    @Schema(description = "Poziom priorytetu zadania (np. HIGH, MEDIUM, LOW). Nie jest persystowany w bazie.",
            example = "HIGH", type = "string", allowableValues = {"HIGH", "MEDIUM", "LOW", "UNDEFINED"})
    private PriorityLevel priority;

    // Konstruktor z opisem, może być przydatny
    public Task(String description) {
        this.description = description;
    }


    // Metoda getPriorityAsString() może być używana do serializacji JSON,
    // aby zwrócić priorytet jako string, zamiast całego obiektu PriorityLevel.
    // W Swaggerze można by odwołać się do tej metody lub użyć typu 'string'.
    @Schema(description = "Priorytet zadania jako ciąg znaków.", example = "HIGH", accessMode = Schema.AccessMode.READ_ONLY)
    public String getPriorityString() {
        return priority != null ? priority.getPriority() : "UNDEFINED";
    }

    // Standardowe gettery i settery są generowane przez Lombok (@Getter, @Setter),
    // ale jeśli potrzebna jest niestandardowa logika, można je zdefiniować ręcznie.
    // public void setPriority(PriorityLevel priority) { this.priority = priority; }
    // public PriorityLevel getPriorityObject() { return priority; } // Jeśli potrzebny jest dostęp do obiektu
}