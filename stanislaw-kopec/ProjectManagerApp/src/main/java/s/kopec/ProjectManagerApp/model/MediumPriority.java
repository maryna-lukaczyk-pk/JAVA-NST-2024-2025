package s.kopec.ProjectManagerApp.model;

import s.kopec.ProjectManagerApp.interfaces.PriorityLevel;

public class MediumPriority implements PriorityLevel {

    @Override
    public String getPriority() {
        return "MEDIUM";
    }
}
