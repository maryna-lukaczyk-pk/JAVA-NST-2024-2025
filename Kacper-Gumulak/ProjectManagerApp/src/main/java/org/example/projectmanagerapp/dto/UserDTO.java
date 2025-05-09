package org.example.projectmanagerapp.dto;

import java.util.List;

// DTO reprezentujący użytkownika w odpowiedziach API
public record UserDTO(Long id, String username, List<Long> project_id) {
}