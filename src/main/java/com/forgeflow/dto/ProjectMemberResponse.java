package com.forgeflow.dto;

import com.forgeflow.entity.ProjectMember;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ProjectMemberResponse {

    private Long id;

    private Long userId;

    private String userName;

    private String userEmail;

    private LocalDateTime joinedAt;

    public ProjectMemberResponse(
            ProjectMember member
    ) {

        this.id = member.getId();

        this.userId =
                member.getUser().getId();

        this.userName =
                member.getUser().getName();

        this.userEmail =
                member.getUser().getEmail();

        this.joinedAt =
                member.getJoinedAt();
    }
}