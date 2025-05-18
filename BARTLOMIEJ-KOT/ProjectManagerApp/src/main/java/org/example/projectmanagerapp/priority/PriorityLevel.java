package org.example.projectmanagerapp.priority;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LowPriority.class, name = "LOW"),
        @JsonSubTypes.Type(value = MediumPriority.class, name = "MEDIUM"),
        @JsonSubTypes.Type(value = HighPriority.class, name = "HIGH")
})


public interface PriorityLevel {
    String getPriority();
}
