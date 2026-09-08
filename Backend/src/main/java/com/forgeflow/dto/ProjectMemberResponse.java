package com.forgeflow.dto;

import com.forgeflow.entity.ProjectMember;
import com.forgeflow.entity.ProjectMemberRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectMemberResponse {

    private Long id;

    private Long userId;

    private String userName;

    private String email;

    private ProjectMemberRole role;

    public ProjectMemberResponse(ProjectMember member) {

        this.id = member.getId();

        this.userId =
                member.getUser().getId();

        this.userName =
                member.getUser().getName();

        this.email =
                member.getUser().getEmail();

        this.role =
                member.getRole();
    }
}