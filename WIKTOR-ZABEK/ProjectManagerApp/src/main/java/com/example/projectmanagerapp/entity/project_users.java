package  com.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.security.SecurityProperties;


@Entity
@Getter
@Setter
@NoArgsConstructor


public class project_users {
    @Id
    @ManyToOne
    @JoinColumn(name = "project_id")
    private projects projects;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private users users;



}

