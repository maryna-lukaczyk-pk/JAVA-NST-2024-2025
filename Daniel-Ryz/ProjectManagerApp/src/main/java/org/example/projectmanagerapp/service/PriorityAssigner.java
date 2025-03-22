package org.example.projectmanagerapp.service;

import org.springframework.stereotype.Component;

@Component
public class PriorityAssigner {

    public PriorityLevel assignPriority(String taskTitle) {
        if (taskTitle.contains("!")) {
            return new HighPriority();
        } else if (taskTitle.length() > 20) {
            return new MediumPriority();
        } else {
            return new LowPriority();
        }
    }
}
