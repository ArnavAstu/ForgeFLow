package com.forgeflow.dto;

import com.forgeflow.entity.Activity;
import com.forgeflow.entity.ActivityType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ActivityResponse {

    private Long id;

    private ActivityType type;

    private String message;

    private Long projectId;

    private Long userId;

    private String userName;

    private Long taskId;

    private LocalDateTime createdAt;


    public ActivityResponse(Activity activity) {

        this.id =
                activity.getId();

        this.type =
                activity.getType();

        this.message =
                activity.getMessage();

        this.projectId =
                activity
                        .getProject()
                        .getId();

        this.userId =
                activity
                        .getUser()
                        .getId();

        this.userName =
                activity
                        .getUser()
                        .getName();

        this.taskId =
                activity.getTaskId();

        this.createdAt =
                activity.getCreatedAt();
    }
}