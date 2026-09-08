package com.forgeflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProjectRequest {

    @NotBlank(message = "Project name is required")
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
}