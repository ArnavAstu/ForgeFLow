package com.forgeflow.dto;

import com.forgeflow.entity.ProjectMemberRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberRoleRequest {

    @NotNull(message = "Role is required")
    private ProjectMemberRole role;
}