package org.example.projectmanagerapp.model;

import org.example.projectmanagerapp.interfaces.PriorityLevel;

public class HighPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "HIGH_PRIORITY";
    }
}