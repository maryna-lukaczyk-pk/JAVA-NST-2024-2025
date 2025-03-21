package s.kopec.ProjectManagerApp.model;

import s.kopec.ProjectManagerApp.interfaces.PriorityLevel;

public class HighPriority implements PriorityLevel {

    @Override
    public String getPriority() {
        return "HIGH";
    }
}
