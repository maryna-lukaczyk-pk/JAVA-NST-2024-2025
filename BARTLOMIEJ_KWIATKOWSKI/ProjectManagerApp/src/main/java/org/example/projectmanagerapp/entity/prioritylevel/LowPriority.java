package org.example.projectmanagerapp.entity.prioritylevel;

public class LowPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "LOW_PRIORITY";
    }
}