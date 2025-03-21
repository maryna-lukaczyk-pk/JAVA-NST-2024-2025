package com.example.projectmanagerapp.entity;

import com.example.projectmanagerapp.entity.PriorityLevel;

public class MediumPriority implements PriorityLevel{
    @Override
    public Integer getPriority() {
        return 2;
    }
}
