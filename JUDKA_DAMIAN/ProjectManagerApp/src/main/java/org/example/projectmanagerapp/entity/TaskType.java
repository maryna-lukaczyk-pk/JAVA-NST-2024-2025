package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Table(name = "tasks")
@NoArgsConstructor

public enum TaskType {
        HIGH,
        MEDIUM,
        LOW
}
