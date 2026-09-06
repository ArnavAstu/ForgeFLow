package com.forgeflow.controller;

import com.forgeflow.dto.CreateTaskRequest;
import com.forgeflow.dto.TaskResponse;
import com.forgeflow.entity.TaskPriority;
import com.forgeflow.entity.TaskStatus;
import com.forgeflow.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        String email =
                authentication.getName();


        TaskResponse response =
                taskService.createTask(
                        projectId,
                        request,
                        email
                );


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // GET PROJECT TASKS
    //
    // Examples:
    //
    // /tasks
    //
    // /tasks?status=TODO
    //
    // /tasks?priority=HIGH
    //
    // /tasks?status=TODO&priority=HIGH
    //
    // /tasks?sortBy=dueDate&direction=asc
    // =========================================================

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getProjectTasks(
            @PathVariable Long projectId,

            @RequestParam(
                    required = false
            )
            TaskStatus status,

            @RequestParam(
                    required = false
            )
            TaskPriority priority,

            @RequestParam(
                    defaultValue = "dueDate"
            )
            String sortBy,

            @RequestParam(
                    defaultValue = "asc"
            )
            String direction,

            Authentication authentication
    ) {

        String email =
                authentication.getName();


        return ResponseEntity.ok(
                taskService.getProjectTasks(
                        projectId,
                        email,
                        status,
                        priority,
                        sortBy,
                        direction
                )
        );
    }
}