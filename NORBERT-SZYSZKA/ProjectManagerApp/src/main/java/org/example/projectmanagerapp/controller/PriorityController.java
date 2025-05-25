package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.priority.PriorityLevel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/priorities")
public class PriorityController {

    private final List<PriorityLevel> priorityLevels;

    public PriorityController(List<PriorityLevel> priorityLevels) {
        this.priorityLevels = priorityLevels;
    }

    @GetMapping
    public List<String> listAllPriorities() {
        return priorityLevels.stream()
                .map(PriorityLevel::getPriority)
                .collect(Collectors.toList());
    }
}
