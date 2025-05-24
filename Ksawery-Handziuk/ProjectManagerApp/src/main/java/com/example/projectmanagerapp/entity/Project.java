// src/main/java/com/example/projectmanagerapp/entity/Project.java
package com.example.projectmanagerapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects; // Dla hashCode i equals

@Entity
@Table(name = "project") // Nazwa tabeli w bazie danych dla projektów
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Reprezentuje projekt w systemie zarządzania projektami.")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unikalny identyfikator projektu, generowany automatycznie.",
            example = "101",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nazwa projektu. Musi być unikalna.",
            example = "System Rezerwacji Biletów",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    // Relacja ManyToMany z User.
    // @JoinTable definiuje tabelę pośredniczącą (łączącą) 'project_app_user'.
    // 'joinColumns' odnosi się do kolumny w tabeli łączącej, która przechowuje ID projektu.
    // 'inverseJoinColumns' odnosi się do kolumny dla ID użytkownika.
    // FetchType.LAZY jest zalecane dla kolekcji, aby uniknąć problemów z wydajnością.
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "project_app_user",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnoreProperties("projects") // Zapobiega cyklicznej serializacji
    @Schema(description = "Zbiór użytkowników powiązanych z tym projektem.")
    private Set<User> users = new HashSet<>();

    public Project(String name) {
        this.name = name;
    }

    // Metody pomocnicze do zarządzania dwukierunkową relacją ManyToMany.
    // Ważne jest, aby aktualizować obie strony relacji.
    public void addUser(User user) {
        this.users.add(user);
        user.getProjects().add(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getProjects().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id) && Objects.equals(name, project.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}