package org.example.projectmanagerapp.dto;

public record TaskDTO( Long id, String title, String description, String taskType, Long projectId, String priority) {
}
