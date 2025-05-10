package  com.example.projectmanagerapp.entity;

import com.example.projectmanagerapp.priority.priority_level;
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
    private String priority;

    private enum taskType{
        NEW,
        OPEN,
        CLOSE,
    }

    @Transient
    private priority_level priority_level;

    void get_priority_level(priority_level priority_level){
    this.priority_level = priority_level;
    this.priority = priority_level.get_priority();


    }

    @ManyToOne
    @JoinColumn(name = "project_id_ref")
    private projects projects;
}