package org.example.projectmanagerapp.entity;

import org.example.projectmanagerapp.priority.PriorityLevel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tasks")

@Getter
@Setter
@NoArgsConstructor

public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private TaskType task_type;

    @Column(name = "priority", nullable = false)
    private String priority;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private Projects project_id;

    public void setPriority(PriorityLevel priorityLevel) {
        this.priority = priorityLevel.getPriority();
    }
}