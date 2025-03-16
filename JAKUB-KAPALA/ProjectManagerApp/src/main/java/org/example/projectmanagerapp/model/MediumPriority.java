package org.example.projectmanagerapp.model;

import org.example.projectmanagerapp.interfaces.PriorityLevel;

public class MediumPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "MEDIUM_PRIORITY";
    }
}