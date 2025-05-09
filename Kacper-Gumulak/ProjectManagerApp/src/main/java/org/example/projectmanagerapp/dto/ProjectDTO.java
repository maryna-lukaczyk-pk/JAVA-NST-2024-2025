package org.example.projectmanagerapp.dto;

import java.util.List;

public record ProjectDTO(Long id, String name, List<Long> userIds, List<Long> taskIds) {
}
