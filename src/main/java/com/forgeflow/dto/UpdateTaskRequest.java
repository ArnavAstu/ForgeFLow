package com.forgeflow.dto;

import com.forgeflow.entity.TaskPriority;
import com.forgeflow.entity.TaskStatus;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskRequest {

    @Size(
            max = 150,
            message = "Task title cannot exceed 150 characters"
    )
    private String title;

    @Size(
            max = 3000,
            message = "Task description cannot exceed 3000 characters"
    )
    private String description;

    private TaskStatus status;

    private TaskPriority priority;
}