package org.example.projectmanagerapp.dto;

import java.util.List;

public record UserDTO(Long id, String username, List<Long> projectIds) {
}
