package org.example.projectmanagerapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.projectmanagerapp.priority.TaskPriority;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private TaskPriority taskType;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference  // Dodane, aby przerwać rekurencję podczas serializacji
    private Project project;
}
