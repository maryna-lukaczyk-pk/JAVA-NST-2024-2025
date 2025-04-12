package org.example.projectmanagerapp.priority;

public enum TaskPriority implements PriorityLevel {
    LOW_PRIORITY {
        @Override
        public String getPriority() {
            return "LOW_PRIORITY";
        }
    },
    MEDIUM_PRIORITY {
        @Override
        public String getPriority() {
            return "MEDIUM_PRIORITY";
        }
    },
    HIGH_PRIORITY {
        @Override
        public String getPriority() {
            return "HIGH_PRIORITY";
        }
    };
}
