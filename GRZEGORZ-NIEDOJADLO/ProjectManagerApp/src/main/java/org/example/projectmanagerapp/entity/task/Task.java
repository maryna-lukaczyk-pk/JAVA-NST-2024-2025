package org.example.projectmanagerapp.entity.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.entity.priority.PriorityLevel;


@Entity (name = "task")
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
    @Column(name = "task_type")
    private TaskType taskType;

    @ManyToOne
    private Project project;

    @Transient
    private PriorityLevel priorityLevel;
}
