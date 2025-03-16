package org.example.projectmanagerapp.utils;

public class LowPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "Low";
    }
}
