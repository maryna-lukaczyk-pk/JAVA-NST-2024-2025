package com.example.projectmanagerapp.entity.priority;

import com.example.projectmanagerapp.PriorityLevel;

public class LowPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "LOW";
    }
}
