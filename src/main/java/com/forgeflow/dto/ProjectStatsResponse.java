package com.forgeflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectStatsResponse {

    private long totalTasks;

    private long todoTasks;

    private long inProgressTasks;

    private long completedTasks;

    private long highPriorityTasks;

    private long assignedTasks;

    private long unassignedTasks;
}