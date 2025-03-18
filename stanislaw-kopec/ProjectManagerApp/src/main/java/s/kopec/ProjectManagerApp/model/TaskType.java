package s.kopec.ProjectManagerApp.model;

import s.kopec.ProjectManagerApp.interfaces.PriorityLevel;

public enum TaskType implements PriorityLevel {
    LOW_PRIORITY(new LowPriority()),
    MEDIUM_PRIORITY(new MediumPriority()),
    HIGH_PRIORITY(new HighPriority());

    private final PriorityLevel priorityLevel;

    TaskType(PriorityLevel priorityLevel) {this.priorityLevel = priorityLevel;}

    @Override
    public String getPriority() {
        return priorityLevel.getPriority();
    }
}