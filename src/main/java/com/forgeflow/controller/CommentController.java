package com.forgeflow.controller;

import com.forgeflow.dto.CommentResponse;
import com.forgeflow.dto.CreateCommentRequest;
import com.forgeflow.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    // =========================================================
    // CREATE COMMENT
    // =========================================================

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long taskId,

            @Valid
            @RequestBody
            CreateCommentRequest request,

            Authentication authentication
    ) {

        String email =
                authentication.getName();

        CommentResponse response =
                commentService.createComment(
                        taskId,
                        request,
                        email
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // GET COMMENTS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getTaskComments(
            @PathVariable Long taskId,
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        return ResponseEntity.ok(
                commentService.getTaskComments(
                        taskId,
                        email
                )
        );
    }


    // =========================================================
    // DELETE COMMENT
    // =========================================================

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        commentService.deleteComment(
                commentId,
                email
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}