package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition="VARCHAR(255)")
    private String name;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "project_user",
            joinColumns = { @JoinColumn(name = "project_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private List<User> users;
}
