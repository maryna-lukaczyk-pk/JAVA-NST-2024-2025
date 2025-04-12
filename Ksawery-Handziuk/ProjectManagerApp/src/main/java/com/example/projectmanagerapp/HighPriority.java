package com.example.projectmanagerapp;

import com.example.projectmanagerapp.PriorityLevel;

public class HighPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "HIGH";
    }
}
