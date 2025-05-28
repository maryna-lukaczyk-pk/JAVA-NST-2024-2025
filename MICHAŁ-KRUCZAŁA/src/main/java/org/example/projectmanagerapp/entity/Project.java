package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @ManyToMany
    @JoinTable(name = "project_user",
            joinColumns = @JoinColumn(name = "projects_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id"))
    private Set<User> users;

    // Konstruktor domyślny
    public Project() {}

    // Konstruktor pełny
    public Project(int id, String name, List<Task> tasks, Set<User> users) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
        this.users = users;
    }

    // Gettery i Settery
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }

    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }

    // Ręcznie zaimplementowany builder
    public static ProjectBuilder builder() {
        return new ProjectBuilder();
    }

    public static class ProjectBuilder {
        private int id;
        private String name;
        private List<Task> tasks;
        private Set<User> users;

        public ProjectBuilder id(int id) {
            this.id = id;
            return this;
        }

        public ProjectBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProjectBuilder tasks(List<Task> tasks) {
            this.tasks = tasks;
            return this;
        }

        public ProjectBuilder users(Set<User> users) {
            this.users = users;
            return this;
        }

        public Project build() {
            return new Project(id, name, tasks, users);
        }
    }
}