package org.example.projectmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private tasktype task_type;
    private enum tasktype {
        NEW, IN_PROGRESS, COMPLETED, FAILED;
    }
    private string priority;

    @Transient
    private PriorityLevel priorityLevel;

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
        this.priority = priorityLevel.getPriorityLevel();
    }

    @ManyToOne
    @JoinColumn(name = "project_id")
    private projects projects;
}