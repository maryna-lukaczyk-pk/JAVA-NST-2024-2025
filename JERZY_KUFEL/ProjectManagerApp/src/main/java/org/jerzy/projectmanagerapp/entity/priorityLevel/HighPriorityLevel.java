package org.jerzy.projectmanager.prioritylevel;

public class HighPriorityLevel implements PriorityLevel {
    @Override
    public String getPriority() {
        return "high";
    }
}