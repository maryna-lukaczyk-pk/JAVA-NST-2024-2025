package org.jerzy.projectmanager.prioritylevel;

public class LowPriorityLevel implements PriorityLevel {
    @Override
    public String getPriority() {
        return "low";
    }
}