package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.projectmanagerapp.enums.TaskType;
import org.example.projectmanagerapp.interfaces.PriorityLevel;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskType task_type;

    @Transient
    private PriorityLevel priorityLevel;

    @ManyToOne
    private Project project;
}
