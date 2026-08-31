package com.forgeflow.dto;

import com.forgeflow.entity.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskRequest {

    @NotBlank(message = "Task title is required")
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

    private TaskPriority priority;
}