package org.example.projectmanagerapp.entity.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.projectmanagerapp.entity.priority.HighPriority;
import org.example.projectmanagerapp.entity.priority.LowPriority;
import org.example.projectmanagerapp.entity.priority.MediumPriority;
import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.entity.enums.TaskType;
import org.example.projectmanagerapp.entity.priority.PriorityLevel;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type")
    private TaskType taskType;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Transient
    private PriorityLevel priorityLevel;

    // Metoda do dynamicznego przypisywania priorytetu
    public void setPriority(String priority) {
        switch (priority.toUpperCase()) {
            case "HIGH":
                this.priorityLevel = new HighPriority();
                break;
            case "MEDIUM":
                this.priorityLevel = new MediumPriority();
                break;
            case "LOW":
                this.priorityLevel = new LowPriority();
                break;
            default:
                throw new IllegalArgumentException("Invalid priority: " + priority);
        }
    }

    // Metoda do pobierania priorytetu
    public String getPriority() {
        return this.priorityLevel != null ? this.priorityLevel.getPriority() : "NOT_SET";
    }
}