package com.example.projectmanagerapp.entity;

import lombok.Getter;

@Getter
public class HighPriority implements PriorityLevel {
    private final String priority = "HIGH";

    @Override
    public String getPriority() {
        return priority;
    }
}