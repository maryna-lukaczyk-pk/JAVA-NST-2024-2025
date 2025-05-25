package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Data;

import lombok.EqualsAndHashCode;
import org.example.projectmanagerapp.priority.PriorityLevel;

@Entity
@Data
@Table(name = "task")
@EqualsAndHashCode(of = "id")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Static (saved in database)
    // tasks priority using ENUMs
    /*
    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private TaskType taskType;
    */

    // Dynamic (not saved in database)
    // tasks priority using @Transient

    @Transient
    private PriorityLevel priorityLevel;

    @ManyToOne
    @JoinColumn(
        name = "project_id",
        foreignKey = @ForeignKey(name = "fk_task_project"),
        nullable = false
    )
    private Project project;

    public String getPriorityLevel() {
        if (priorityLevel == null) {
            return "Not set";
        } else {
            return priorityLevel.getPriority();
        }
    }

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }
}