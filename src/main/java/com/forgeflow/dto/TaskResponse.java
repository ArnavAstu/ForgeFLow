package com.forgeflow.dto;

import com.forgeflow.entity.Task;
import com.forgeflow.entity.TaskPriority;
import com.forgeflow.entity.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TaskResponse {

    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private Long projectId;

    private String projectName;

    private Long assignedUserId;

    private String assignedUserName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public TaskResponse(Task task) {

        this.id = task.getId();

        this.title = task.getTitle();

        this.description = task.getDescription();

        this.status = task.getStatus();

        this.priority = task.getPriority();

        this.projectId = task.getProject().getId();

        this.projectName = task.getProject().getName();

        if (task.getAssignedUser() != null) {

            this.assignedUserId =
                    task.getAssignedUser().getId();

            this.assignedUserName =
                    task.getAssignedUser().getName();
        }

        this.createdAt = task.getCreatedAt();

        this.updatedAt = task.getUpdatedAt();
    }
}