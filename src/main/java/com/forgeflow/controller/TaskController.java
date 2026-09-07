package com.forgeflow.controller;

import com.forgeflow.dto.CreateTaskRequest;
import com.forgeflow.dto.TaskResponse;
import com.forgeflow.dto.UpdateTaskRequest;
import com.forgeflow.entity.TaskPriority;
import com.forgeflow.entity.TaskStatus;
import com.forgeflow.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;


    // =========================================================
    // CREATE TASK
    // =========================================================

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateTaskRequest request,
            Authentication authentication
    ) {

        TaskResponse response =
                taskService.createTask(
                        projectId,
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // GET PROJECT TASKS
    // =========================================================

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getProjectTasks(

            @PathVariable Long projectId,

            @RequestParam(required = false)
            TaskStatus status,

            @RequestParam(required = false)
            TaskPriority priority,

            @RequestParam(defaultValue = "dueDate")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                taskService.getProjectTasks(
                        projectId,
                        authentication.getName(),
                        status,
                        priority,
                        sortBy,
                        direction,
                        page,
                        size
                )
        );
    }


    // =========================================================
    // GET ONE TASK
    // =========================================================

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(

            @PathVariable Long projectId,

            @PathVariable Long taskId,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                taskService.getTask(
                        taskId,
                        authentication.getName()
                )
        );
    }


    // =========================================================
    // UPDATE TASK
    // =========================================================

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(

            @PathVariable Long projectId,

            @PathVariable Long taskId,

            @Valid @RequestBody UpdateTaskRequest request,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                taskService.updateTask(
                        taskId,
                        request,
                        authentication.getName()
                )
        );
    }


    // =========================================================
    // DELETE TASK
    // =========================================================

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(

            @PathVariable Long projectId,

            @PathVariable Long taskId,

            Authentication authentication
    ) {

        taskService.deleteTask(
                taskId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }


    // =========================================================
    // ASSIGN TASK
    // =========================================================

    @PatchMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<TaskResponse> assignTask(

            @PathVariable Long projectId,

            @PathVariable Long taskId,

            @PathVariable Long userId,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                taskService.assignTask(
                        taskId,
                        userId,
                        authentication.getName()
                )
        );
    }


    // =========================================================
    // UNASSIGN TASK
    // =========================================================

    @DeleteMapping("/{taskId}/assign")
    public ResponseEntity<TaskResponse> unassignTask(

            @PathVariable Long projectId,

            @PathVariable Long taskId,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                taskService.unassignTask(
                        taskId,
                        authentication.getName()
                )
        );
    }
}