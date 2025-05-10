package org.example.projectmanagerapp.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;;

@Embeddable

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class ProjectUserId implements Serializable {
    private Long project_id;
    private Long user_id;
}
