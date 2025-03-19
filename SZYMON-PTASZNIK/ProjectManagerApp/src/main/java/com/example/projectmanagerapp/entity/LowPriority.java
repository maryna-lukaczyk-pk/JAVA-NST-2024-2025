package com.example.projectmanagerapp.entity;

public class LowPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "Low";
    }
}
