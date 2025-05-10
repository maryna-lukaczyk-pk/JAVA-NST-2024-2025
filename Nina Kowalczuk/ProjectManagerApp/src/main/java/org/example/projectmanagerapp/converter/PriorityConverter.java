package org.example.projectmanagerapp.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.projectmanagerapp.priority.HighPriority;
import org.example.projectmanagerapp.priority.LowPriority;
import org.example.projectmanagerapp.priority.MediumPriority;
import org.example.projectmanagerapp.priority.PriorityLevel;

@Converter(autoApply = false)
public class PriorityConverter implements AttributeConverter<PriorityLevel, String> {

    @Override
    public String convertToDatabaseColumn(PriorityLevel attribute) {
        return attribute != null ? attribute.getPriority() : null;
    }

    @Override
    public PriorityLevel convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        switch (dbData) {
            case "HIGH":
                return new HighPriority();
            case "MEDIUM":
                return new MediumPriority();
            case "LOW":
                return new LowPriority();
            default:
                throw new IllegalArgumentException("Nieznany priorytet: " + dbData);
        }
    }
}
