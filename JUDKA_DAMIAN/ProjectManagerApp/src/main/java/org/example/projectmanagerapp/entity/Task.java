package org.example.projectmanagerapp.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private TaskType taskType;

    @Transient
    @JsonIgnore
    private PriorityLevel priorityLevel;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;

        switch (taskType) {
            case HIGH -> this.priorityLevel = new HighPriority();
            case MEDIUM -> this.priorityLevel = new MediumPriority();
            case LOW -> this.priorityLevel = new LowPriority();
        }
    }
}