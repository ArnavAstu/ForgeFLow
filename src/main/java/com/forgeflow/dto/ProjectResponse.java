package com.forgeflow.dto;

import com.forgeflow.entity.Project;
import com.forgeflow.entity.ProjectStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ProjectResponse {

    private Long id;

    private String name;

    private String description;

    private ProjectStatus status;

    private Long ownerId;

    private String ownerName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public ProjectResponse(Project project) {

        this.id = project.getId();

        this.name = project.getName();

        this.description = project.getDescription();

        this.status = project.getStatus();

        this.ownerId = project.getOwner().getId();

        this.ownerName = project.getOwner().getName();

        this.createdAt = project.getCreatedAt();

        this.updatedAt = project.getUpdatedAt();
    }
}