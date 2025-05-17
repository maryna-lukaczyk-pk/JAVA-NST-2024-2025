package com.example.projectmanagerapp.entity;

import com.example.projectmanagerapp.priority.*;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor

@Table(name = "tasks")
public class Task
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //@Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private task_type task_type;

    @Transient
    private PriorityLevel priorityLevel;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public Task(int i, String s) {
    this.id = i;
    this.title = s;
}

    public void setPriorityLevel() {
        if (this.task_type == null) {
            priorityLevel = new MediumPriority();
            return;
        }
        switch (task_type) {
            case high -> priorityLevel = new HighPriority();
            case medium -> priorityLevel = new MediumPriority();
            case low -> priorityLevel = new LowPriority();
            default -> priorityLevel = new MediumPriority();
        }
    }
}