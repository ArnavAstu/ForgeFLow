package com.forgeflow.dto;

import com.forgeflow.entity.TaskPriority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateTaskRequest {

    @NotBlank(
            message = "Task title is required"
    )
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


    @NotNull(
            message = "Task priority is required"
    )
    private TaskPriority priority;


    @FutureOrPresent(
            message = "Due date cannot be in the past"
    )
    private LocalDateTime dueDate;
}