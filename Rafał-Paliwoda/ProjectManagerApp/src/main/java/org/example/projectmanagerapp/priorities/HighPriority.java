package org.example.projectmanagerapp.priorities;

import org.example.projectmanagerapp.interfaces.PriorityLevel;

public class HighPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "High Priority";
    }
}
