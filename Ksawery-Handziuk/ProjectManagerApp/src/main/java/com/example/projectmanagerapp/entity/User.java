// src/main/java/com/example/projectmanagerapp/entity/User.java
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
@Table(name = "app_user") // Nazwa tabeli w bazie danych dla użytkowników
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Reprezentuje użytkownika w systemie zarządzania projektami.")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unikalny identyfikator użytkownika, generowany automatycznie.",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY) // Tylko do odczytu przez API
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Unikalna nazwa użytkownika. Musi być unikalna wśród wszystkich użytkowników.",
            example = "jacek123",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    // Relacja ManyToMany z Project. 'mappedBy = "users"' wskazuje, że tabela łącząca
    // jest zarządzana przez pole 'users' w encji Project.
    // FetchType.LAZY oznacza, że projekty nie będą ładowane automatycznie przy pobieraniu użytkownika,
    // tylko wtedy, gdy jawnie się o nie poprosi (np. przez user.getProjects()).
    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("users") // Zapobiega cyklicznej serializacji przy pobieraniu projektów z użytkownikami
    @Schema(description = "Zbiór projektów, z którymi użytkownik jest powiązany.",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Set<Project> projects = new HashSet<>();

    public User(String username) {
        this.username = username;
    }

    // Metody hashCode i equals są ważne dla poprawnego działania kolekcji Set
    // oraz dla porównywania obiektów User.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}