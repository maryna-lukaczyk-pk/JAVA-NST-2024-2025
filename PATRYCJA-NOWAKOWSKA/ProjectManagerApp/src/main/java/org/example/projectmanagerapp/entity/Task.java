package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @ManyToOne
    private Project project;

    @ManyToOne
    private User assignedUser;

    // Nowe pole dla poziomu priorytetu
    private int priorityLevel;

    // Metoda przypisująca poziom priorytetu
    public void assignPriorityLevel(int priority) {
        this.priorityLevel = priority;
    }
}
