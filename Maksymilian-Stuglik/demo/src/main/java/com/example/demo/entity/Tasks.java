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
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private taskType taskType;
    @Column(name = "projectId", insertable = false, updatable = false)
    private Long projectId;
    private String priority;

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
}