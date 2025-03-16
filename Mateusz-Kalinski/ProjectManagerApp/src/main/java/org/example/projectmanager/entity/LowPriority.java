package org.example.projectmanager.entity;

import lombok.Getter;

@Getter
public class LowPriority implements PriorityLevel {
    private final String priority = "LOW";

    @Override
    public String getPriority() {
        return priority;
    }
}