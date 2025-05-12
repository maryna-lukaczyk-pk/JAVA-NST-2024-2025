package com.example.projectmanagerapp.priority;


public enum PriorityLevelEnum implements PriorityLevel {
    HIGH(new HighPriority()),
    MEDIUM(new MediumPriority()),
    LOW(new LowPriority());

    private final PriorityLevel priorityLevel;

    PriorityLevelEnum(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    @Override
    public String getPriority() {
        return priorityLevel.getPriority();
    }
}