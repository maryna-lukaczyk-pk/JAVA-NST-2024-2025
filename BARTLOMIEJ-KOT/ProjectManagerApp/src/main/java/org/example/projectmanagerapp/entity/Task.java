package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.projectmanagerapp.priority.PriorityLevel;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Transient
    private PriorityLevel priorityLevel;

    public Task(String name, PriorityLevel priorityLevel) {
        this.name = name;
        this.priorityLevel = priorityLevel;
    }

    public String getPriority() {
        return priorityLevel.getPriority();
    }
}
