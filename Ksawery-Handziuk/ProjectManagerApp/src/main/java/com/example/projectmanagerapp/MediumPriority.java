package com.example.projectmanagerapp;

import com.example.projectmanagerapp.PriorityLevel;

public class MediumPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "MEDIUM";
    }
}
