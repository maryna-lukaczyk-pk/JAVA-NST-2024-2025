package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "project_user")

@Getter
@Setter
@NoArgsConstructor

public class ProjectUser {
    @EmbeddedId
    private ProjectUserId id;

    @ManyToOne
    @MapsId("project_id")
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private Projects project_id;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Users user_id;
}
