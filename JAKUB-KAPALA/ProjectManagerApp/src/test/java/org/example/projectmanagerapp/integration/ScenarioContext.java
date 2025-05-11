package org.example.projectmanagerapp.integration;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ScenarioContext {
    public Project project;
    public User user;
    public Task task;
    public String uniqueProjectName;
    public String uniqueUsername;
}
