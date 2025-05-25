package org.example.projectmanagerapp.priority;

import org.springframework.stereotype.Component;

@Component
public class MediumPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "Medium Priority";
    }
}