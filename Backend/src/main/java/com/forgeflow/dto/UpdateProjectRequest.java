package com.forgeflow.dto;

import com.forgeflow.entity.ProjectStatus;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProjectRequest {

    @Size(
            max = 100,
            message = "Project name cannot exceed 100 characters"
    )
    private String name;

    @Size(
            max = 2000,
            message = "Description cannot exceed 2000 characters"
    )
    private String description;

    private ProjectStatus status;
}