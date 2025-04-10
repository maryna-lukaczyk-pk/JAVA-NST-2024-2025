package org.example.projectmanagerapp.priorities;

import org.example.projectmanagerapp.interfaces.PriorityLevel;

public class LowPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "Low Priority";
    }
}
