package org.example.projectmanagerapp.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.projectmanagerapp.priority.HighPriority;
import org.example.projectmanagerapp.priority.LowPriority;
import org.example.projectmanagerapp.priority.MediumPriority;
import org.example.projectmanagerapp.priority.PriorityLevel;

@Converter(autoApply = false)
public class PriorityLevelConverter implements AttributeConverter<PriorityLevel, String> {
    @Override
    public String convertToDatabaseColumn(PriorityLevel priorityLevel) {
        if (priorityLevel == null) {
            return null;
        }
        return priorityLevel.getPriority(); // np. "HIGH", "MEDIUM", "LOW"
    }

    @Override
    public PriorityLevel convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return switch (dbData) {
            case "HIGH" -> new HighPriority();
            case "MEDIUM" -> new MediumPriority();
            case "LOW" -> new LowPriority();
            default -> throw new IllegalArgumentException("Nieznany priorytet: " + dbData);
        };
    }

}