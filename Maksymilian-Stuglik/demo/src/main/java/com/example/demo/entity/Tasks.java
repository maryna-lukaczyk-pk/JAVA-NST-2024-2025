package com.example.demo.entity;

import com.example.demo.priority.HighPriority;
import com.example.demo.priority.LowPriority;
import com.example.demo.priority.MediumPriority;
import com.example.demo.priority.PriorityLevel;
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

    // Explicit setter for id
    public void setId(Long id) {
        this.id = id;
    }

    // Explicit getter for id
    public Long getId() {
        return id;
    }
    private String title;
    private String description;

    // Explicit setter for title
    public void setTitle(String title) {
        this.title = title;
    }

    // Explicit getter for title
    public String getTitle() {
        return title;
    }

    // Explicit setter for description
    public void setDescription(String description) {
        this.description = description;
    }

    // Explicit getter for description
    public String getDescription() {
        return description;
    }
    @Enumerated(EnumType.STRING)
    private taskType taskType;
    @Column(name = "projectId", insertable = false, updatable = false)
    private Long projectId;
    private String priority;

    // Explicit getter for priority
    public String getPriority() {
        return priority;
    }

    private enum taskType {
        NEW,
        IN_PROGRESS,
        DONE;
    }
    @Transient
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
