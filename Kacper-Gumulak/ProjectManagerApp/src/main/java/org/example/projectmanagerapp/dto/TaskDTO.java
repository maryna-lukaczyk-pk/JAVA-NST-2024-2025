package org.example.projectmanagerapp.dto;

// DTO reprezentujący zadanie w odpowiedziach API
public record TaskDTO( Long id, String title, String description, String task_type, Long project_id, String priority) {
}