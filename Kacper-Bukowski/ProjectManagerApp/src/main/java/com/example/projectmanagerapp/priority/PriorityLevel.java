package com.example.projectmanagerapp.priority;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "priority")
@JsonSubTypes({
    @JsonSubTypes.Type(value = HighPriority.class, name = "HIGH"),
    @JsonSubTypes.Type(value = MediumPriority.class, name = "MEDIUM"),
    @JsonSubTypes.Type(value = LowPriority.class, name = "LOW")
})
public interface PriorityLevel {
    String getPriority();
}