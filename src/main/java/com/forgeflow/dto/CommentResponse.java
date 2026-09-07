package com.forgeflow.dto;

import com.forgeflow.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponse {

    private Long id;

    private String content;

    private Long taskId;

    private Long userId;

    private String userName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public CommentResponse(Comment comment) {

        this.id = comment.getId();

        this.content = comment.getContent();

        this.taskId =
                comment.getTask().getId();

        this.userId =
                comment.getUser().getId();

        this.userName =
                comment.getUser().getName();

        this.createdAt =
                comment.getCreatedAt();

        this.updatedAt =
                comment.getUpdatedAt();
    }
}