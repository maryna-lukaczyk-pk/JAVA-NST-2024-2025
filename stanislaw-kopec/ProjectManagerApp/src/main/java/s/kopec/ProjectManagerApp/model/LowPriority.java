package s.kopec.ProjectManagerApp.model;

import s.kopec.ProjectManagerApp.interfaces.PriorityLevel;

public class LowPriority implements PriorityLevel {

    @Override
    public String getPriority() {
        return "LOW";
    }
}
