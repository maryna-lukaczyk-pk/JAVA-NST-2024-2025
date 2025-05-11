
package org.example.projectmanagerapp.entity;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany
    @JoinTable(
            name = "project_user",
            joinColumns = @JoinColumn(
                    name = "project_id",
                    foreignKey = @ForeignKey(name = "fk_project_user_project")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id",
                    foreignKey = @ForeignKey(name = "fk_project_user_user")
            )
    )
    private Set<User> users;
    @OneToMany(mappedBy = "project")
    private List<Task> tasks;
}

