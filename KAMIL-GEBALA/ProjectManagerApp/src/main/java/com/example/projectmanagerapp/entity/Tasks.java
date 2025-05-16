package  com.example.projectmanagerapp.entity;

import com.example.projectmanagerapp.priority.PriorityLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor


public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskType task_type;

    public enum TaskType {
        NEW, IN_PROGRESS, COMPLETED, FAILED;
    }

    private String priority;

    @Transient
    @com.fasterxml.jackson.annotation.JsonIgnore
    private PriorityLevel priorityLevel;

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
        this.priority = priorityLevel.getPriorityLevel();
    }

    @ManyToOne
    @JoinColumn(name = "projectId")
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getTask_type() {
        return task_type;
    }

    public void setTask_type(TaskType task_type) {
        this.task_type = task_type;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
