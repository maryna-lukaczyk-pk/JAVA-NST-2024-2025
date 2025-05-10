package org.example.projectmanagerapp.entity.prioritylevel;

public class HighPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "HIGH_PRIORITY";
    }
}