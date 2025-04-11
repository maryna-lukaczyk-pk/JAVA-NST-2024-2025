package org.example.projectmanager.entity;

import lombok.Getter;

@Getter
public class MediumPriority implements PriorityLevel {
    private final String priority = "MEDIUM";

    @Override
    public String getPriority() {
        return priority;
    }
}