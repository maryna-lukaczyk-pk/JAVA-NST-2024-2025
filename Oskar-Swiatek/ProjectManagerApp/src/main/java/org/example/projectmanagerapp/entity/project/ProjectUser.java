package org.example.projectmanagerapp.entity.project;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.projectmanagerapp.entity.user.User;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "project_users")
public class ProjectUser {
    @EmbeddedId
    private ProjectUserID id;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
}

@Embeddable
@Getter
@Setter
@NoArgsConstructor
class ProjectUserID implements Serializable {
    private Long projectId;
    private Long userId;
}
