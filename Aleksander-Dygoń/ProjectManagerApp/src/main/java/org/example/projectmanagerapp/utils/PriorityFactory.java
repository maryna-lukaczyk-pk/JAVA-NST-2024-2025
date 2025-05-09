package org.example.projectmanagerapp.utils;

public class PriorityFactory {
    public static PriorityLevel createPriority(String level) {
        switch(level.toLowerCase()) {
            case "high":
                return new HighPriority();
            case "medium":
                return new MediumPriority();
            case "low":
                return new LowPriority();
            default:
                return new MediumPriority();
        }
    }
}