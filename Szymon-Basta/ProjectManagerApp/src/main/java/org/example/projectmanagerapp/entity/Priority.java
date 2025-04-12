package org.example.projectmanagerapp.entity;

public class Priority {
    public static class HighPriority implements PriorityLevel {
        @Override
        public String getPriority() {
            return "High";
        }
    }

    public static class MediumPriority implements PriorityLevel {
        @Override
        public String getPriority() {
            return "Medium";
        }
    }

    public static class LowPriority implements PriorityLevel {
        @Override
        public String getPriority() {
            return "Low";
        }
    }
}
