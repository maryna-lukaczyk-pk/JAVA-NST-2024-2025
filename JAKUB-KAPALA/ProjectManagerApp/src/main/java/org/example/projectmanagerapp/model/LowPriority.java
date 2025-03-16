package org.example.projectmanagerapp.model;

import org.example.projectmanagerapp.interfaces.PriorityLevel;

public class LowPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "LOW";
    }
}