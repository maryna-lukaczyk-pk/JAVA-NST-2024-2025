package org.example.projectmanagerapp.entity;

import java.util.Set;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public enum TaskType {
    BUG,
    FEATURE,
    IMPROVEMENT;
}