package  com.example.projectmanagerapp.entity;

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
    private taskType taskType;
    private Long project_id;

    private enum taskType{
        NEW,
        OPEN,
        CLOSE,
    }

    @ManyToOne
    @JoinColumn(name = "project_id")
    private projects projects;
}