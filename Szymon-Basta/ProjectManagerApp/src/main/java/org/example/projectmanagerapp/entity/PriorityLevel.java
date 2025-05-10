package org.example.projectmanagerapp.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Priority.HighPriority.class, name = "high"),
        @JsonSubTypes.Type(value = Priority.MediumPriority.class, name = "medium"),
        @JsonSubTypes.Type(value = Priority.LowPriority.class, name = "low")
})
public interface PriorityLevel {
    String getPriority();
}