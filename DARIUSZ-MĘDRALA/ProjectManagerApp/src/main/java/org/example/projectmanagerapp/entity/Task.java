package org.example.projectmanagerapp.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tasks")

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition="TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private Project project;
}



