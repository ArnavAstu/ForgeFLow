package com.forgeflow.controller;

import com.forgeflow.dto.TaskResponse;
import com.forgeflow.dto.UpdateTaskRequest;
import com.forgeflow.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskManagementController {

    private final TaskService taskService;


    // GET TASK
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(
            @PathVariable Long taskId,
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        return ResponseEntity.ok(
                taskService.getTask(
                        taskId,
                        email
                )
        );
    }


    // UPDATE TASK
    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequest request,
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        return ResponseEntity.ok(
                taskService.updateTask(
                        taskId,
                        request,
                        email
                )
        );
    }


    // DELETE TASK
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long taskId,
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        taskService.deleteTask(
                taskId,
                email
        );

        return ResponseEntity
                .noContent()
                .build();
    }


    // ASSIGN TASK
    @PutMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<TaskResponse> assignTask(
            @PathVariable Long taskId,
            @PathVariable Long userId,
            Authentication authentication
    ) {

        String ownerEmail =
                authentication.getName();

        return ResponseEntity.ok(
                taskService.assignTask(
                        taskId,
                        userId,
                        ownerEmail
                )
        );
    }


    // UNASSIGN TASK
    @DeleteMapping("/{taskId}/assignment")
    public ResponseEntity<TaskResponse> unassignTask(
            @PathVariable Long taskId,
            Authentication authentication
    ) {

        String ownerEmail =
                authentication.getName();

        return ResponseEntity.ok(
                taskService.unassignTask(
                        taskId,
                        ownerEmail
                )
        );
    }
}