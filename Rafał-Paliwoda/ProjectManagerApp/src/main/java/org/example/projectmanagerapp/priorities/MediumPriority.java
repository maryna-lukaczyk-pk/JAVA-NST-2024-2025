package org.example.projectmanagerapp.priorities;

import org.example.projectmanagerapp.interfaces.PriorityLevel;

public class MediumPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "Medium Priority";
    }
}
