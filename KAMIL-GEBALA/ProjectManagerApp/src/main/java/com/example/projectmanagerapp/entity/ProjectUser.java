package  com.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(ProjectUser.ProjectUserId.class)
public class ProjectUser {
    @Id
    @ManyToOne
    @JoinColumn(name = "projectId")
    @com.fasterxml.jackson.annotation.JsonBackReference("project-users")
    private Project project;

    @Id
    @ManyToOne
    @JoinColumn(name = "userId")
    @com.fasterxml.jackson.annotation.JsonBackReference("user-projects")
    private Users user;

    public static class ProjectUserId implements Serializable {
        private Long project;
        private Long user;

        public ProjectUserId() {}

        public ProjectUserId(Long project, Long user) {
            this.project = project;
            this.user = user;
        }

        public Long getProject() {
            return project;
        }

        public void setProject(Long project) {
            this.project = project;
        }

        public Long getUser() {
            return user;
        }

        public void setUser(Long user) {
            this.user = user;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProjectUserId that = (ProjectUserId) o;
            return project.equals(that.project) && user.equals(that.user);
        }

        @Override
        public int hashCode() {
            return project.hashCode() ^ user.hashCode();
        }
    }
}
