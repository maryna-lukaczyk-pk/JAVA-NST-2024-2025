package org.jerzy.projectmanagerapp.priorityLevel;

public class LowPriorityLevel implements PriorityLevel {
    @Override
    public String getPriority() {
        return "low";
    }
}