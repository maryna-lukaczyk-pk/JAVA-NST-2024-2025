package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.projectmanagerapp.priority.HighPriority;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType taskType;

    @Column(nullable = false)
    private String priority;

    @ManyToOne(optional = true)
    @JoinColumn(name = "project_id", nullable = true)
    private Project project;

    @PrePersist
    @PreUpdate
    private void applyDefaults() {
        if (this.taskType == null) {
            this.taskType = TaskType.TODO;
        }
        if (this.priority == null) {
            this.priority = new HighPriority().getPriority();
        }
    }
}