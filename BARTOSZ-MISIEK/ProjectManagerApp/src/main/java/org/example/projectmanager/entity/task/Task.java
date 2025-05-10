package org.example.projectmanager.entity.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.projectmanager.entity.prioritylevel.PriorityLevel;
import org.example.projectmanager.entity.project.Project;

@Entity(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @ManyToOne
    private Project project;

    @Transient
    private PriorityLevel priorityLevel;
}
