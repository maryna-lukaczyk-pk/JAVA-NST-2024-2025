package org.example.projectmanagerapp.dto;

import java.util.List;

// DTO reprezentujący projekt w odpowiedziach API
public record ProjectDTO(Long id, String name, List<Long> user_id, List<Long> task_id) {
}