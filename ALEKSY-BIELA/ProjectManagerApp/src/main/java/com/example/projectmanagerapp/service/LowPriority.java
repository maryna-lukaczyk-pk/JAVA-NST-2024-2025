package com.example.projectmanagerapp.service;

public class LowPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "Low";
    }
}
