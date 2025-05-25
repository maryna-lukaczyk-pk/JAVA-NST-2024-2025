package com.example.demo.entity;

import com.example.demo.priority.HighPriority;
import com.example.demo.priority.LowPriority;
import com.example.demo.priority.MediumPriority;
import com.example.demo.priority.PriorityLevel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Text;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    private String title;
    private String description;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    @Enumerated(EnumType.STRING)
    private taskType taskType;
    @Column(name = "projectId", insertable = false, updatable = false)
    private Long projectId;
    private String priority;

    public String getPriority() {
        return priority;
    }

    private enum taskType {
        NEW,
        IN_PROGRESS,
        DONE;
    }
    @Transient
    @com.fasterxml.jackson.annotation.JsonIgnore
    private PriorityLevel priorityLevel;

    public void setPriority(String priority) {
        this.priority = priority;

        if (priority != null) {
            switch(priority.toUpperCase()) {
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
                    // Domyślny priorytet
                    this.priorityLevel = new MediumPriority();
            }
        }
    }
    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
        this.priority = priorityLevel.getPriority();
    }
    @ManyToOne
    @JoinColumn(name = "projectId")
    @JsonBackReference("project-tasks")
    private Project project;

    // Explicit setter for project
    public void setProject(Project project) {
        this.project = project;
    }

    // Explicit getter for project
    public Project getProject() {
        return project;
    }
}
