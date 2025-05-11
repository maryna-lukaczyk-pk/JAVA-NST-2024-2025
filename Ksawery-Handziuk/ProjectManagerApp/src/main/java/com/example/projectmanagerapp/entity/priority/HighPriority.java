package com.example.projectmanagerapp.entity.priority;

import com.example.projectmanagerapp.PriorityLevel;

public class HighPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "HIGH";
    }
}
